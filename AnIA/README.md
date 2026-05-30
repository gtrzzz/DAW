# AnIA

Fecha de inicio: 30/05/2026

AnIA es un proyecto para crear un asistente de IA local accesible desde Telegram y desde una pagina web privada.

## Objetivo

Usar el PC principal como motor de IA con Ollama y un portatil antiguo como servidor permanente para:

- Bot de Telegram.
- Pagina web de chat.
- Backend comun para hablar con Ollama.
- Panel de estado y administracion.
- Automatizaciones futuras como Wake-on-LAN.

## Arquitectura Prevista

```text
Telegram ------\
                > Portatil Linux servidor -> PC principal con Ollama
Web local -----/
```

## Carpetas

```text
app/       Codigo del backend y bot.
web/       Interfaz web.
docs/      Documentacion tecnica paso a paso.
scripts/   Scripts de instalacion, comprobacion y mantenimiento.
systemd/   Servicios para Linux.
obsidian/  Notas y plantillas para abrir como vault de Obsidian.
```

## Estado Actual

- Repositorio local inicializado.
- Estructura base creada.
- Documentacion inicial creada.
- Repositorio conectado y subido a GitHub.
- PC principal probado con Ollama en red local.
- Modelo `llama3.1:8b` instalado y probado.
- Prueba desde Linux Mint realizada correctamente.
- Portatil servidor preparado con Linux Mint e IP `192.168.1.138`.
- Backend comun inicial creado con FastAPI.
- Pendiente integrar bot y web.
- Pendiente crear regla de firewall de Windows como administrador.

## Reglas Del Proyecto

- Usar fechas en formato `dia/mes/ano`.
- No subir tokens, contrasenas ni archivos `.env` reales.
- Documentar cada fase antes o durante la implementacion.
- Mantener la arquitectura simple y mantenible.
