from typing import Any

import httpx

from app.config import settings


class OllamaError(RuntimeError):
    pass


async def list_models() -> dict[str, Any]:
    try:
        async with httpx.AsyncClient(timeout=10) as client:
            response = await client.get(f"{settings.ollama_url}/api/tags")
            response.raise_for_status()
            return response.json()
    except httpx.HTTPError as exc:
        raise OllamaError(f"No se pudo conectar con Ollama: {exc}") from exc


async def generate_response(prompt: str) -> str:
    payload = {
        "model": settings.ollama_model,
        "prompt": prompt,
        "system": settings.ollama_system_prompt,
        "stream": False,
    }

    try:
        async with httpx.AsyncClient(timeout=settings.request_timeout) as client:
            response = await client.post(f"{settings.ollama_url}/api/generate", json=payload)
            response.raise_for_status()
            data = response.json()
    except httpx.HTTPError as exc:
        raise OllamaError(f"No se pudo generar respuesta con Ollama: {exc}") from exc

    return str(data.get("response", "")).strip()
