# Modelos De Ollama

Fecha: 30/05/2026

## Objetivo

Elegir modelos adecuados para AnIA segun calidad, velocidad y memoria disponible.

El PC principal tiene una NVIDIA RTX 5070 con 12 GB de VRAM. Esto permite usar modelos de 7B, 8B, 14B y algunos modelos MoE mas grandes si la cuantizacion y descarga son adecuadas.

## Modelos Instalados

```text
llama3.2:3b
llama3.1:8b
qwen2.5:14b
qwen3:30b-a3b
```

## Recomendacion Actual

Para respuestas con mas conocimiento general y mejor razonamiento:

```text
qwen3:30b-a3b
```

Alternativa equilibrada si `qwen3:30b-a3b` resulta demasiado pesado:

```text
qwen2.5:14b
```

Para respuestas mas rapidas:

```text
llama3.1:8b
```

## Comparativa Practica

| Modelo | Calidad | Velocidad | Uso recomendado |
|---|---|---|---|
| `llama3.2:3b` | Baja-media | Muy rapida | Pruebas rapidas |
| `llama3.1:8b` | Buena | Rapida | Uso diario equilibrado |
| `qwen2.5:14b` | Mejor | Mas lenta | Conocimiento general y respuestas mas completas |
| `qwen3:30b-a3b` | Avanzada | Mas lenta | Mejor razonamiento y modelo mas reciente |

## Instalar Modelo Superior

En el PC principal:

```powershell
ollama pull qwen2.5:14b
```

Para el modelo avanzado Qwen3 MoE:

```powershell
ollama pull qwen3:30b-a3b
```

Validado el 30/05/2026:

```text
qwen2.5:14b instalado y probado correctamente.
qwen3:30b-a3b instalado y probado correctamente.
```

Prueba realizada:

```powershell
curl http://192.168.1.133:11434/api/generate `
  -d '{"model":"qwen2.5:14b","system":"Responde en espanol claro y directo.","prompt":"Di solo OK si funcionas.","stream":false}'
```

Resultado:

```text
OK
```

Prueba de `qwen3:30b-a3b` realizada:

```powershell
curl http://192.168.1.133:11434/api/generate `
  -d '{"model":"qwen3:30b-a3b","system":"Responde en espanol claro y directo.","prompt":"Di solo OK si funcionas.","stream":false}'
```

Resultado:

```text
OK
```

Nota: Qwen3 puede devolver un campo adicional `thinking` en la API de Ollama. AnIA usa el campo `response`, por lo que Telegram debe mostrar la respuesta final, no el razonamiento interno.

## Cambiar Modelo En El Portatil

Editar `.env`:

```bash
cd ~/proyectos/DAW/AnIA
nano .env
```

Cambiar:

```env
OLLAMA_MODEL=llama3.1:8b
```

Por:

```env
OLLAMA_MODEL=qwen3:30b-a3b
```

Reiniciar el bot:

```bash
Ctrl+C
python -m app.telegram_bot
```

Si tambien esta corriendo la API, reiniciarla para que cargue el nuevo `.env`.

## Nota Sobre Conocimiento

Un modelo con mas parametros suele razonar mejor y tener mas capacidad, pero no garantiza conocimiento actualizado. El conocimiento del modelo depende de sus datos de entrenamiento.

Mas adelante, para que AnIA sepa sobre documentos concretos, apuntes, repositorios o informacion propia, habra que implementar RAG:

```text
documentos propios -> indice/vector database -> busqueda -> contexto para Ollama
```

Esto es distinto a cambiar de modelo y sera una fase futura.
