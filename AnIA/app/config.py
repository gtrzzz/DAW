import os
from dataclasses import dataclass

from dotenv import load_dotenv


load_dotenv()


@dataclass(frozen=True)
class Settings:
    app_name: str = os.getenv("APP_NAME", "AnIA")
    telegram_bot_token: str = os.getenv("TELEGRAM_BOT_TOKEN", "")
    ollama_url: str = os.getenv("OLLAMA_URL", "http://192.168.1.133:11434").rstrip("/")
    ollama_model: str = os.getenv("OLLAMA_MODEL", "llama3.1:8b")
    ollama_system_prompt: str = os.getenv(
        "OLLAMA_SYSTEM_PROMPT",
        "Eres AnIA, una asistente de IA local. Responde siempre en espanol claro, directo y util. "
        "Si el usuario pide una prueba sencilla, responde exactamente a la prueba sin rechazarla.",
    )
    request_timeout: float = float(os.getenv("OLLAMA_TIMEOUT", "120"))


settings = Settings()
