# Bot De Telegram

Fecha: 30/05/2026

## Objetivo

Integrar Telegram con AnIA para poder hablar con Ollama desde el movil.

El bot se ejecuta en el portatil servidor y llama a Ollama en el PC principal.

## Seguridad Del Token

El token real del bot no debe subirse a GitHub.

Debe guardarse solo en el archivo `.env` del portatil:

```env
TELEGRAM_BOT_TOKEN=token_real_aqui
```

Si el token se comparte por error en chats, capturas o repositorios, se recomienda regenerarlo en BotFather.

## Instalar Dependencias

En el portatil:

```bash
cd ~/proyectos/DAW/AnIA
git pull
source .venv/bin/activate
pip install -r requirements.txt
```

## Configurar `.env`

```bash
nano .env
```

Contenido minimo:

```env
APP_NAME=AnIA
TELEGRAM_BOT_TOKEN=token_real_aqui
OLLAMA_URL=http://192.168.1.133:11434
OLLAMA_MODEL=llama3.1:8b
OLLAMA_TIMEOUT=120
```

Comprobar que Git no quiere subir `.env`:

```bash
git status --short
```

No debe aparecer `.env`.

## Ejecutar Bot Manualmente

```bash
cd ~/proyectos/DAW/AnIA
source .venv/bin/activate
python -m app.telegram_bot
```

La terminal quedara ocupada mientras el bot este activo. Para pararlo:

```text
Ctrl+C
```

## Comandos Del Bot

```text
/start   Mensaje inicial.
/health  Comprueba que el bot esta vivo y muestra el modelo configurado.
```

Los mensajes normales se envian a Ollama y la respuesta vuelve por Telegram.

## Prueba Recomendada

Enviar al bot desde Telegram:

```text
/health
```

Despues enviar:

```text
Responde solo con OK en espanol.
```

Resultado esperado:

```text
OK
```

## Problemas Comunes

Si el bot dice que no puede contactar con Ollama:

- Comprobar que el PC principal esta encendido.
- Comprobar que Ollama escucha en `192.168.1.133:11434`.
- Probar desde el portatil: `curl http://192.168.1.133:11434/api/tags`.

Si el bot no arranca:

- Revisar que `.env` existe.
- Revisar que `TELEGRAM_BOT_TOKEN` esta definido.
- Revisar que las dependencias estan instaladas.

## Error `Unauthorized`

Si aparece un error similar a:

```text
telegram.error.InvalidToken
telegram.error.Unauthorized
Unauthorized
```

La causa mas probable es que Telegram esta rechazando el token.

Comprobar que el token esta cargado sin mostrarlo completo:

```bash
python - <<'PY'
from dotenv import load_dotenv
import os

load_dotenv()
token = os.getenv('TELEGRAM_BOT_TOKEN', '')
print('token definido:', bool(token))
print('longitud:', len(token))
print('empieza por:', token[:8])
PY
```

Probar el token directamente contra Telegram:

```bash
source .env
curl "https://api.telegram.org/bot${TELEGRAM_BOT_TOKEN}/getMe"
```

Resultado correcto:

```json
{"ok":true,...}
```

Resultado incorrecto:

```json
{"ok":false,"error_code":401,"description":"Unauthorized"}
```

Si sale `Unauthorized`, regenerar el token en BotFather y actualizar `.env`.

## Siguiente Paso

Cuando el bot funcione manualmente, se creara un servicio `systemd` para que arranque solo al encender el portatil.
