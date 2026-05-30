# Vision General

Fecha: 30/05/2026

## Resumen

AnIA sera un sistema de IA local compuesto por dos maquinas:

- PC principal: ejecuta Ollama y los modelos de IA usando la GPU NVIDIA.
- Portatil servidor: ejecuta el bot de Telegram, la web y el backend comun.

## Flujo Principal

```text
Usuario -> Telegram o Web -> Backend en portatil -> Ollama en PC -> Respuesta
```

## Componentes

- Ollama: motor de modelos locales.
- Backend: API comun para web y Telegram.
- Bot Telegram: canal de chat desde el movil.
- Web: canal de chat desde navegador.
- Nginx: servidor web y proxy inverso en Linux.
- systemd: arranque automatico de servicios en Linux.

## Decisiones Iniciales

- El portatil sera servidor permanente.
- El PC principal seguira haciendo la inferencia pesada.
- GitHub sera la fuente oficial del codigo y documentacion tecnica.
- Obsidian se usara como libreta del proyecto.
