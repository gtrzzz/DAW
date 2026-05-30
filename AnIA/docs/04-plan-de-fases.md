# Plan De Fases

Fecha: 30/05/2026

## Fase 0: Gestion Del Proyecto

- Crear repositorio local.
- Crear estructura de carpetas.
- Crear documentacion inicial.
- Conectar con GitHub.
- Abrir vault en Obsidian.

## Fase 1: PC Principal Con Ollama

- Configurar Ollama para red local. Estado: completado.
- Abrir firewall de Windows. Estado: pendiente de PowerShell como administrador.
- Probar `http://192.168.1.133:11434/api/tags`. Estado: completado.
- Instalar modelo recomendado. Estado: completado con `llama3.1:8b`.

## Fase 2: Portatil Servidor

- Instalar Debian 12 o Ubuntu Server.
- Activar SSH.
- Fijar IP local.
- Probar conexion con Ollama.

## Fase 3: Backend Comun

- Crear API.
- Crear cliente Ollama.
- Crear endpoint de chat.
- Crear endpoint de estado.

## Fase 4: Telegram

- Integrar bot.
- Reutilizar backend o cliente comun.
- Crear servicio systemd.

## Fase 5: Web

- Crear interfaz web.
- Conectar con API.
- Servir con Nginx.

## Fase 6: Produccion Local

- Servicios systemd.
- Logs.
- Seguridad basica.
- Backups.

## Fase 7: Extras

- Wake-on-LAN.
- Selector de modelo.
- Historial.
- Login web.
- Tailscale o Cloudflare Tunnel.
