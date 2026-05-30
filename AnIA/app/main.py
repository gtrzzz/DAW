from fastapi import FastAPI, HTTPException
from pydantic import BaseModel, Field

from app.config import settings
from app.ollama_client import OllamaError, generate_response, list_models
from app.rag import index_knowledge, rag_answer


app = FastAPI(title=settings.app_name)


class ChatRequest(BaseModel):
    message: str = Field(min_length=1, max_length=8000)


class ChatResponse(BaseModel):
    response: str
    model: str


class RagResponse(BaseModel):
    response: str
    model: str
    sources: list[dict[str, object]]


class RagIndexResponse(BaseModel):
    files: int
    chunks: int


@app.get("/api/health")
async def health() -> dict[str, str]:
    return {
        "status": "ok",
        "app": settings.app_name,
    }


@app.get("/api/status")
async def status() -> dict[str, object]:
    try:
        models = await list_models()
    except OllamaError as exc:
        raise HTTPException(status_code=503, detail=str(exc)) from exc

    return {
        "status": "ok",
        "ollama_url": settings.ollama_url,
        "ollama_model": settings.ollama_model,
        "models": models.get("models", []),
    }


@app.post("/api/chat", response_model=ChatResponse)
async def chat(request: ChatRequest) -> ChatResponse:
    try:
        response = await generate_response(request.message)
    except OllamaError as exc:
        raise HTTPException(status_code=503, detail=str(exc)) from exc

    return ChatResponse(response=response, model=settings.ollama_model)


@app.post("/api/rag/index", response_model=RagIndexResponse)
async def rag_index() -> RagIndexResponse:
    try:
        result = await index_knowledge()
    except OllamaError as exc:
        raise HTTPException(status_code=503, detail=str(exc)) from exc

    return RagIndexResponse(files=result["files"], chunks=result["chunks"])


@app.post("/api/rag/chat", response_model=RagResponse)
async def rag_chat(request: ChatRequest) -> RagResponse:
    try:
        response, sources = await rag_answer(request.message)
    except OllamaError as exc:
        raise HTTPException(status_code=503, detail=str(exc)) from exc

    return RagResponse(
        response=response,
        model=settings.ollama_model,
        sources=[source.__dict__ for source in sources],
    )
