from dataclasses import dataclass
from pathlib import Path

import chromadb

from app.config import settings
from app.ollama_client import generate_embedding, generate_response


SUPPORTED_EXTENSIONS = {".md", ".txt"}


@dataclass(frozen=True)
class RagSource:
    path: str
    topic: str
    source_level: str
    source_name: str
    category: str
    chunk: int


def get_collection():
    settings.chroma_dir.mkdir(parents=True, exist_ok=True)
    client = chromadb.PersistentClient(path=str(settings.chroma_dir))
    return client.get_or_create_collection(name=settings.rag_collection)


def chunk_text(text: str, max_chars: int = 1600, overlap: int = 200) -> list[str]:
    clean_text = "\n".join(line.strip() for line in text.splitlines() if line.strip())
    if not clean_text:
        return []

    chunks: list[str] = []
    start = 0
    while start < len(clean_text):
        end = min(start + max_chars, len(clean_text))
        chunks.append(clean_text[start:end])
        if end == len(clean_text):
            break
        start = max(0, end - overlap)

    return chunks


def build_metadata(relative_path: Path, chunk_index: int) -> dict[str, str | int]:
    parts = relative_path.parts
    topic = parts[0] if parts else "general"
    source_level = parts[1] if len(parts) > 1 else "sin-nivel"
    source_name = parts[2] if len(parts) > 2 else topic
    category = parts[3] if len(parts) > 3 else topic

    return {
        "path": relative_path.as_posix(),
        "topic": topic,
        "source_level": source_level,
        "source_name": source_name,
        "category": category,
        "chunk": chunk_index,
    }


def iter_knowledge_files() -> list[Path]:
    if not settings.knowledge_dir.exists():
        return []

    return sorted(
        path
        for path in settings.knowledge_dir.rglob("*")
        if path.is_file() and path.suffix.lower() in SUPPORTED_EXTENSIONS and path.name.lower() != "readme.md"
    )


async def index_knowledge() -> dict[str, int]:
    collection = get_collection()
    files = iter_knowledge_files()
    indexed_chunks = 0

    for file_path in files:
        relative_path = file_path.relative_to(settings.knowledge_dir)
        text = file_path.read_text(encoding="utf-8", errors="ignore")

        for index, chunk in enumerate(chunk_text(text)):
            document_id = f"{relative_path.as_posix()}::{index}"
            embedding = await generate_embedding(chunk)
            collection.upsert(
                ids=[document_id],
                documents=[chunk],
                embeddings=[embedding],
                metadatas=[build_metadata(relative_path, index)],
            )
            indexed_chunks += 1

    return {"files": len(files), "chunks": indexed_chunks}


async def retrieve_context(question: str, top_k: int | None = None) -> tuple[str, list[RagSource]]:
    collection = get_collection()
    query_embedding = await generate_embedding(question)
    results = collection.query(
        query_embeddings=[query_embedding],
        n_results=top_k or settings.rag_top_k,
    )

    documents = results.get("documents", [[]])[0]
    metadatas = results.get("metadatas", [[]])[0]

    context_parts: list[str] = []
    sources: list[RagSource] = []
    for document, metadata in zip(documents, metadatas, strict=False):
        if not document or not isinstance(metadata, dict):
            continue
        source = RagSource(
            path=str(metadata.get("path", "desconocido")),
            topic=str(metadata.get("topic", "general")),
            source_level=str(metadata.get("source_level", "sin-nivel")),
            source_name=str(metadata.get("source_name", "desconocida")),
            category=str(metadata.get("category", "general")),
            chunk=int(metadata.get("chunk", 0)),
        )
        sources.append(source)
        context_parts.append(
            "\n".join(
                [
                    f"Fuente: {source.path}",
                    f"Nivel: {source.source_level}",
                    f"Origen: {source.source_name}",
                    f"Categoria: {source.category}",
                    str(document),
                ]
            )
        )

    return "\n\n---\n\n".join(context_parts), sources


async def rag_answer(question: str) -> tuple[str, list[RagSource]]:
    context, sources = await retrieve_context(question)
    if not context:
        return "No he encontrado documentos RAG relevantes. Primero indexa contenido en knowledge/.", []

    prompt = f"""
Usa el siguiente contexto para responder. Si el contexto no contiene la respuesta, dilo claramente.
Prioriza fuentes de mayor calidad: nivel-1 sobre nivel-2, nivel-2 sobre nivel-3, y notas-propias solo como complemento.
Si hay conflicto entre fuentes, explica la diferencia y da mas peso a revisiones sistematicas, metaanalisis y position stands.
Responde en espanol, de forma clara, util y prudente. No des consejo medico personalizado; recomienda profesional sanitario si hay patologia, medicacion o riesgo.

Contexto:
{context}

Pregunta:
{question}
""".strip()
    answer = await generate_response(prompt)
    return answer, sources
