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

## Primera Implementacion

Estado: preparada el 30/05/2026.

Componentes añadidos:

```text
app/rag.py                 Logica RAG.
scripts/ingest_rag.py      Indexador de documentos.
knowledge/                 Carpetas tematicas locales.
data/chroma/               Base vectorial local, ignorada por Git.
```

Endpoints añadidos:

```text
POST /api/rag/index
POST /api/rag/chat
```

Comando Telegram añadido:

```text
/rag pregunta usando documentos
```

## Temas Iniciales

Se han creado carpetas para estos temas:

```text
programacion
ia
musica
coches
algeciras
diseno-grafico
blender
java
html
css
guitarra
spotify
recetas-cocina
fitness
hipertrofia
nutricion
```

Cada carpeta puede contener documentos `.md` o `.txt`.

## Instalar Dependencias

En el portatil:

```bash
cd ~/proyectos/DAW/AnIA
git pull
source .venv/bin/activate
pip install -r requirements.txt
```

## Modelo De Embeddings

La primera version usa embeddings desde Ollama:

```env
RAG_EMBED_MODEL=nomic-embed-text
```

Antes de indexar documentos hay que instalar el modelo de embeddings en el PC principal:

```powershell
ollama pull nomic-embed-text
```

No es un modelo de chat. Solo sirve para convertir texto en vectores de busqueda.

## Añadir Documentos

Ejemplo para hipertrofia:

```bash
cd ~/proyectos/DAW/AnIA
nano knowledge/hipertrofia/conceptos-basicos.md
```

Ejemplo de contenido:

```markdown
# Hipertrofia - Conceptos Basicos

La hipertrofia muscular es el aumento del tamano de las fibras musculares.
Factores clave: tension mecanica, volumen efectivo, proximidad al fallo, recuperacion y nutricion.
```

## Indexar Documentos

```bash
cd ~/proyectos/DAW/AnIA
source .venv/bin/activate
python -m scripts.ingest_rag
```

Resultado esperado:

```text
Archivos indexados: N
Fragmentos indexados: N
```

## Consultar RAG Por API

```bash
curl http://127.0.0.1:8000/api/rag/chat \
  -H "Content-Type: application/json" \
  -d '{"message":"Que sabes sobre hipertrofia segun mis documentos?"}'
```

## Consultar RAG Por Telegram

```text
/rag Que sabes sobre hipertrofia segun mis documentos?
```

## Privacidad

La carpeta `knowledge/` esta configurada para no subir documentos privados a GitHub.

Se suben solo los `README.md` de estructura.

La base vectorial local `data/chroma/` tampoco se sube.

## RAG No Es Entrenamiento

RAG no entrena el modelo ni modifica sus pesos. Lo que hace es buscar informacion relevante en tus documentos y pasarla como contexto al modelo.

Ventaja:

- Mas seguro.
- Mas rapido.
- Reversible.
- Permite fuentes concretas.

Entrenar o ajustar un modelo seria otra fase distinta y mucho mas costosa.

## Fases RAG

### Fase RAG 1: Prototipo Local

- Crear carpeta `knowledge/` ignorada por Git si contiene documentos privados. Estado: completado.
- Indexar archivos `.md` y `.txt`. Estado: completado.
- Generar embeddings. Estado: completado mediante Ollama.
- Guardar indice local. Estado: completado con ChromaDB.
- Crear endpoint `/api/rag/chat`. Estado: completado.

### Fase RAG 2: PDFs Y Documentacion

- Extraer texto de PDFs.
- Trocear documentos en chunks.
- Guardar metadatos de fuente. Estado: primera version implementada con `source_level`, `source_name` y `category` segun la ruta del documento.
- Devolver citas o referencias.

### Fase RAG Fitness/Nutricion

- Estructura por niveles de fuente creada en `knowledge/fitness/`.
- Documento de trabajo creado en `docs/12-rag-fitness-nutricion.md`.
- El prompt RAG prioriza fuentes `nivel-1` sobre divulgacion y notas propias.

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
