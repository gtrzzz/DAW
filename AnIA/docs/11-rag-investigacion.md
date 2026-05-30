# RAG Para AnIA

Fecha: 30/05/2026

## Objetivo

Investigar e implementar RAG para que AnIA pueda responder usando conocimiento propio y actualizado.

RAG significa `Retrieval-Augmented Generation`. En vez de depender solo de lo que sabe el modelo, AnIA buscara informacion relevante en documentos o fuentes externas y se la pasara a Ollama como contexto.

## Por Que Hace Falta

Cambiar a un modelo mas grande mejora razonamiento y conocimiento general, pero no garantiza:

- Datos actualizados a 2026.
- Conocimiento de documentos propios.
- Informacion exacta de proyectos personales.
- Respuestas verificables con fuentes.

RAG permite añadir:

- PDFs.
- Apuntes.
- Documentacion tecnica.
- Repositorios.
- Paginas web guardadas.
- Notas de Obsidian.

## Arquitectura Prevista

```text
Usuario -> Telegram/Web -> AnIA -> Busqueda RAG -> Contexto -> Ollama -> Respuesta
```

## Componentes A Investigar

### Carga De Documentos

Formatos iniciales:

- `.md`
- `.txt`
- `.pdf`
- `.html`

### Embeddings

Modelos candidatos en Ollama:

- `nomic-embed-text`
- `mxbai-embed-large`

### Base Vectorial

Opciones candidatas:

- ChromaDB: sencilla para empezar.
- Qdrant: mejor para crecer y usar como servicio.
- SQLite + extension vectorial: posible, pero menos directa.

Recomendacion inicial:

```text
ChromaDB
```

Motivo: simple, local y suficiente para una primera version.

## Fases RAG

### Fase RAG 1: Prototipo Local

- Crear carpeta `knowledge/` ignorada por Git si contiene documentos privados.
- Indexar archivos `.md` y `.txt`.
- Generar embeddings.
- Guardar indice local.
- Crear endpoint `/api/rag/chat`.

### Fase RAG 2: PDFs Y Documentacion

- Extraer texto de PDFs.
- Trocear documentos en chunks.
- Guardar metadatos de fuente.
- Devolver citas o referencias.

### Fase RAG 3: Integracion Con Telegram

- Comando `/rag` para preguntar usando documentos.
- O modo automatico segun configuracion.

### Fase RAG 4: Actualizacion De Fuentes

- Script para reindexar.
- Posible indexacion programada con systemd timer.

## Preguntas De Diseno Pendientes

- Que documentos debe conocer AnIA primero.
- Si el RAG sera privado o se subiran documentos de ejemplo al repo.
- Si queremos respuestas con fuentes visibles.
- Si el bot debe usar RAG siempre o solo con un comando.

## Recomendacion Inicial

Empezar con:

```text
Ollama embeddings: nomic-embed-text
Vector DB: ChromaDB
Documentos iniciales: Markdown y TXT
```

Despues ampliar a PDF.
