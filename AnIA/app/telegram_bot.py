import logging

from telegram import Update
from telegram.constants import ChatAction
from telegram.error import InvalidToken, TelegramError
from telegram.ext import Application, CommandHandler, ContextTypes, MessageHandler, filters

from app.config import settings
from app.ollama_client import OllamaError, generate_response
from app.rag import rag_answer


logging.basicConfig(
    format="%(asctime)s %(levelname)s %(name)s: %(message)s",
    level=logging.INFO,
)
logger = logging.getLogger(__name__)


async def start(update: Update, context: ContextTypes.DEFAULT_TYPE) -> None:
    del context
    if update.message is None:
        return

    await update.message.reply_text(
        "Hola, soy AnIA. Enviame un mensaje y respondere usando la IA local."
    )


async def health(update: Update, context: ContextTypes.DEFAULT_TYPE) -> None:
    del context
    if update.message is None:
        return

    await update.message.reply_text(
        f"AnIA esta activa. Modelo actual: {settings.ollama_model}"
    )


async def rag(update: Update, context: ContextTypes.DEFAULT_TYPE) -> None:
    if update.message is None or update.effective_chat is None:
        return

    question = " ".join(context.args).strip()
    if not question:
        await update.message.reply_text("Uso: /rag pregunta usando tus documentos")
        return

    await context.bot.send_chat_action(chat_id=update.effective_chat.id, action=ChatAction.TYPING)

    try:
        response, sources = await rag_answer(question)
    except OllamaError:
        logger.exception("Error al consultar RAG/Ollama")
        await update.message.reply_text(
            "No puedo consultar el RAG ahora mismo. Comprueba Ollama y que los documentos esten indexados."
        )
        return

    if sources:
        source_lines = "\n".join(f"- {source.path}" for source in sources[:5])
        response = f"{response}\n\nFuentes:\n{source_lines}"

    await update.message.reply_text(response)


async def handle_message(update: Update, context: ContextTypes.DEFAULT_TYPE) -> None:
    if update.message is None or update.message.text is None or update.effective_chat is None:
        return

    message = update.message.text.strip()
    if not message:
        return

    await context.bot.send_chat_action(chat_id=update.effective_chat.id, action=ChatAction.TYPING)

    try:
        response = await generate_response(message)
    except OllamaError:
        logger.exception("Error al consultar Ollama")
        await update.message.reply_text(
            "No puedo contactar ahora mismo con Ollama. Comprueba que el PC principal esta encendido."
        )
        return

    if not response:
        response = "Ollama no devolvio respuesta."

    await update.message.reply_text(response)


def main() -> None:
    if not settings.telegram_bot_token:
        raise RuntimeError("Falta TELEGRAM_BOT_TOKEN en el archivo .env")

    try:
        application = Application.builder().token(settings.telegram_bot_token).build()
    except InvalidToken as exc:
        raise RuntimeError(
            "El token de Telegram no es valido. Revisa TELEGRAM_BOT_TOKEN en .env o regeneralo en BotFather."
        ) from exc

    application.add_handler(CommandHandler("start", start))
    application.add_handler(CommandHandler("health", health))
    application.add_handler(CommandHandler("rag", rag))
    application.add_handler(MessageHandler(filters.TEXT & ~filters.COMMAND, handle_message))

    logger.info("Iniciando bot de Telegram de AnIA")
    try:
        application.run_polling(allowed_updates=Update.ALL_TYPES)
    except InvalidToken as exc:
        raise RuntimeError(
            "Telegram ha rechazado el token. Revisa TELEGRAM_BOT_TOKEN en .env o crea uno nuevo con BotFather."
        ) from exc
    except TelegramError as exc:
        raise RuntimeError(f"Error de Telegram al iniciar el bot: {exc}") from exc


if __name__ == "__main__":
    main()
