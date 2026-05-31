# RAG Fitness, Nutricion E Hipertrofia

Fecha: 31/05/2026

## Objetivo

Crear un RAG serio para nutricion, hipertrofia y deporte, priorizando evidencia cientifica y evitando contaminar la base con contenido SEO o influencers genericos.

## Estructura

```text
knowledge/fitness/
├── nivel-1/
│   ├── pubmed/
│   │   ├── hipertrofia/
│   │   ├── nutricion/
│   │   └── suplementacion/
│   ├── pmc/
│   └── examine/
├── nivel-2/
│   ├── stronger-by-science/
│   ├── rp-strength/
│   └── barbell-medicine/
├── nivel-3/
│   └── youtube-fiable/
└── notas-propias/
```

## Regla Principal

El RAG debe responder dando mas peso a:

- `nivel-1`: PubMed, PMC, Examine, revisiones sistematicas, metaanalisis y position stands.
- `nivel-2`: divulgacion basada en evidencia como Stronger By Science, RP Strength y Barbell Medicine.
- `nivel-3`: YouTube fiable, solo si el contenido esta bien identificado y revisado.
- `notas-propias`: utiles para preferencias personales, pero no como evidencia primaria.

## Que Meter Primero

Empieza por pocos documentos de alta calidad:

- Creatina: Examine y revisiones de PubMed/PMC.
- Proteina e hipertrofia: revisiones de Stuart Phillips, Schoenfeld y Helms.
- Volumen y frecuencia: Schoenfeld, Stronger By Science y RP Strength.
- Cafeina: Examine y metaanalisis.
- Perdida de grasa: revisiones sobre deficit calorico, proteina y entrenamiento de fuerza.

## Que No Meter

- TikTok fitness.
- Influencers genericos.
- Blogs de suplementos.
- Men's Health.
- MyProtein Blog.
- Gymshark Blog.
- Webs SEO tipo listas genericas.

## Formato Recomendado

Usa `knowledge/fitness/plantilla-documento-rag.md` como base para cada documento.

Cada archivo debe incluir:

- Fuente.
- URL.
- Nivel.
- Categoria.
- Fecha de publicacion.
- Fecha de consulta.
- Tipo de evidencia.
- Resumen.
- Limitaciones.
- Aplicacion practica.

## Indexar

En el portatil Linux Mint:

```bash
cd ~/proyectos/DAW/AnIA
source .venv/bin/activate
python -m scripts.ingest_rag
```

## Consultar

Por Telegram:

```text
/rag Que dice la evidencia sobre creatina para hipertrofia?
```

Por API:

```bash
curl http://127.0.0.1:8000/api/rag/chat \
  -H "Content-Type: application/json" \
  -d '{"message":"Que dice la evidencia sobre proteina e hipertrofia?"}'
```

## Siguiente Mejora

La siguiente fase logica es añadir soporte para PDF y HTML guardado, para meter papers completos de PMC y articulos largos sin convertirlos manualmente a Markdown.
