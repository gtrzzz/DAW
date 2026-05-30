# Servicios systemd 24/7

Fecha: 30/05/2026

## Objetivo

Ejecutar AnIA en el portatil servidor sin depender de terminales abiertas.

Servicios previstos:

```text
ania-api@usuario.service       Backend FastAPI.
ania-telegram@usuario.service  Bot de Telegram.
```

Se usan servicios parametrizados con `%i`, donde `%i` es el usuario de Linux.

Ejemplo si el usuario del portatil es `zzz`:

```text
ania-api@zzz.service
ania-telegram@zzz.service
```

## Requisitos Previos

- Repositorio clonado en `~/proyectos/DAW/AnIA`.
- `.venv` creado.
- Dependencias instaladas con `pip install -r requirements.txt`.
- `.env` configurado.
- Bot probado manualmente.
- Backend probado manualmente.

## Actualizar Repo En El Portatil

```bash
cd ~/proyectos/DAW/AnIA
git pull
source .venv/bin/activate
pip install -r requirements.txt
```

## Instalar Servicios

Desde el portatil:

```bash
cd ~/proyectos/DAW/AnIA
sudo cp systemd/ania-api.service /etc/systemd/system/ania-api@.service
sudo cp systemd/ania-telegram.service /etc/systemd/system/ania-telegram@.service
sudo systemctl daemon-reload
```

## Activar Arranque Automatico

Sustituir `TU_USUARIO` por el usuario real de Linux.

Ver usuario actual:

```bash
whoami
```

Activar servicios:

```bash
sudo systemctl enable ania-api@TU_USUARIO.service
sudo systemctl enable ania-telegram@TU_USUARIO.service
```

Ejemplo:

```bash
sudo systemctl enable ania-api@zzz.service
sudo systemctl enable ania-telegram@zzz.service
```

## Arrancar Servicios

```bash
sudo systemctl start ania-api@TU_USUARIO.service
sudo systemctl start ania-telegram@TU_USUARIO.service
```

## Ver Estado

```bash
sudo systemctl status ania-api@TU_USUARIO.service
sudo systemctl status ania-telegram@TU_USUARIO.service
```

## Ver Logs En Tiempo Real

API:

```bash
journalctl -u ania-api@TU_USUARIO.service -f
```

Telegram:

```bash
journalctl -u ania-telegram@TU_USUARIO.service -f
```

## Reiniciar Despues De Cambios

Cuando cambie `.env` o el codigo:

```bash
sudo systemctl restart ania-api@TU_USUARIO.service
sudo systemctl restart ania-telegram@TU_USUARIO.service
```

Si solo cambia el modelo para Telegram, basta con reiniciar el bot:

```bash
sudo systemctl restart ania-telegram@TU_USUARIO.service
```

## Parar Servicios

```bash
sudo systemctl stop ania-api@TU_USUARIO.service
sudo systemctl stop ania-telegram@TU_USUARIO.service
```

## Comprobaciones

API desde el portatil:

```bash
curl http://127.0.0.1:8000/api/health
```

API desde Windows:

```powershell
curl http://192.168.1.138:8000/api/health
```

Bot desde Telegram:

```text
/health
```

## Problemas Comunes

Si el servicio no arranca, revisar logs:

```bash
journalctl -u ania-telegram@TU_USUARIO.service -n 100 --no-pager
```

Tambien revisar el servicio API si falla:

```bash
journalctl -u ania-api@TU_USUARIO.service -n 100 --no-pager
```

### Ya Hay Un Proceso Manual Corriendo

Si antes se arranco manualmente el bot o la API, detener esas terminales con:

```text
Ctrl+C
```

Comprobar si el puerto `8000` ya esta ocupado:

```bash
sudo ss -ltnp | grep ':8000'
```

Comprobar procesos de AnIA:

```bash
ps aux | grep -E 'uvicorn|telegram_bot' | grep -v grep
```

Si hay procesos manuales antiguos, cerrarlos antes de arrancar systemd.

### Reinicio Limpio

```bash
sudo systemctl stop ania-api@TU_USUARIO.service
sudo systemctl stop ania-telegram@TU_USUARIO.service
sudo systemctl reset-failed ania-api@TU_USUARIO.service
sudo systemctl reset-failed ania-telegram@TU_USUARIO.service
sudo systemctl start ania-api@TU_USUARIO.service
sudo systemctl start ania-telegram@TU_USUARIO.service
```

Si aparece `Falta TELEGRAM_BOT_TOKEN`, revisar `.env`.

Si aparece error de conexion con Ollama, comprobar que el PC principal esta encendido y que responde:

```bash
curl http://192.168.1.133:11434/api/tags
```
