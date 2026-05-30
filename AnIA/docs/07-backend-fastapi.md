# Backend Comun Con FastAPI

Fecha: 30/05/2026

## Objetivo

Crear una API comun para que la web y el bot de Telegram usen la misma logica para hablar con Ollama.

## Componentes

```text
app/main.py           API FastAPI.
app/config.py         Configuracion desde variables de entorno.
app/ollama_client.py  Cliente HTTP para Ollama.
requirements.txt      Dependencias Python.
```

## Variables De Entorno

El archivo real `.env` no se sube al repositorio.

Ejemplo:

```env
APP_NAME=AnIA
TELEGRAM_BOT_TOKEN=pendiente
OLLAMA_URL=http://192.168.1.133:11434
OLLAMA_MODEL=llama3.1:8b
OLLAMA_TIMEOUT=120
```

## Instalar Dependencias En El Portatil

Desde Linux Mint:

```bash
cd ~/proyectos/DAW/AnIA
source .venv/bin/activate
pip install -r requirements.txt
```

## Arrancar Backend En Desarrollo

```bash
cd ~/proyectos/DAW/AnIA
source .venv/bin/activate
uvicorn app.main:app --host 0.0.0.0 --port 8000 --reload
```

## Endpoints

Comprobar salud de la API:

```bash
curl http://127.0.0.1:8000/api/health
```

Comprobar estado de Ollama:

```bash
curl http://127.0.0.1:8000/api/status
```

Enviar mensaje:

```bash
curl http://127.0.0.1:8000/api/chat \
  -H "Content-Type: application/json" \
  -d '{"message":"Responde solo con OK en espanol."}'
```

## Probar Desde El PC Principal

Si el backend corre en el portatil `192.168.1.138`:

```powershell
curl http://192.168.1.138:8000/api/health
curl http://192.168.1.138:8000/api/status
```

## Firewall Del Portatil

Para acceder al backend desde otros dispositivos de la red local:

```bash
sudo ufw allow 8000/tcp
sudo ufw status verbose
```

En produccion local, mas adelante se usara Nginx en el puerto `80` y el puerto `8000` podra quedar solo para uso interno.

## Seguridad

- No subir `.env`.
- No guardar tokens en codigo ni documentacion.
- No exponer el backend directamente a internet.
- Usar red local, Tailscale o Cloudflare Tunnel cuando toque acceso remoto.

## Validacion En Linux Mint

Fecha: 30/05/2026

Validado en el portatil servidor `192.168.1.138`:

- `/api/health` responde `status: ok`.
- `/api/status` responde correctamente y detecta Ollama.
- `/api/chat` responde correctamente usando `llama3.1:8b`.

Prueba realizada:

```bash
curl http://127.0.0.1:8000/api/chat \
  -H "Content-Type: application/json" \
  -d '{"message":"Responde solo con OK en espanol."}'
```

Resultado validado:

```text
response: OK
```
