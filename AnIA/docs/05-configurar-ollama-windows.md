# Configurar Ollama En Windows

Fecha: 30/05/2026

## Objetivo

Preparar el PC principal para que el portatil servidor pueda conectarse a Ollama por red local.

## Datos De Esta Maquina

- Sistema: Windows 11 Pro.
- Ollama: `0.24.0`.
- IP local por Wi-Fi: `192.168.1.133`.
- Puerto Ollama: `11434`.
- URL local para el portatil: `http://192.168.1.133:11434`.

## Modelos Instalados

```text
llama3.1:8b
llama3.2:3b
```

Modelo recomendado para AnIA:

```text
llama3.1:8b
```

## Comprobaciones Realizadas

Comprobar version:

```powershell
ollama --version
```

Listar modelos:

```powershell
ollama list
```

Probar API local:

```powershell
Invoke-RestMethod -Uri "http://localhost:11434/api/tags"
```

Probar API por IP LAN:

```powershell
Invoke-RestMethod -Uri "http://192.168.1.133:11434/api/tags"
```

Probar generacion con `llama3.1:8b`:

```powershell
Invoke-RestMethod `
  -Uri "http://192.168.1.133:11434/api/generate" `
  -Method Post `
  -ContentType "application/json" `
  -Body '{"model":"llama3.1:8b","prompt":"Responde solo con OK en espanol.","stream":false}'
```

Resultado esperado:

```text
OK
```

## Configurar Ollama Para Red Local

La variable necesaria es:

```text
OLLAMA_HOST=0.0.0.0:11434
```

Se ha guardado a nivel de usuario con:

```powershell
setx OLLAMA_HOST "0.0.0.0:11434"
```

Importante: `setx` no cambia el entorno de la terminal actual. Para que la aplicacion de Ollama lo lea de forma normal, cerrar sesion, reiniciar Windows o cerrar completamente Ollama y abrirlo desde una sesion nueva.

## Arranque Manual Validado

Para validar la configuracion sin reiniciar Windows:

```powershell
$env:OLLAMA_HOST = "0.0.0.0:11434"
Get-Process -Name ollama -ErrorAction SilentlyContinue | Stop-Process
Start-Sleep -Seconds 3
Start-Process -FilePath "$env:LOCALAPPDATA\Programs\Ollama\ollama.exe" -ArgumentList "serve" -WindowStyle Hidden
```

Comprobar escucha en red:

```powershell
netstat -ano | findstr ":11434"
```

Resultado correcto:

```text
0.0.0.0:11434 LISTENING
```

## Firewall De Windows

Esta parte requiere PowerShell como administrador.

Abrir PowerShell como administrador y ejecutar:

```powershell
New-NetFirewallRule `
  -DisplayName "AnIA Ollama LAN" `
  -Direction Inbound `
  -Protocol TCP `
  -LocalPort 11434 `
  -Action Allow `
  -Profile Private
```

Cuando sepamos la IP fija del portatil, es mejor restringir la regla solo a esa IP. Ejemplo si el portatil queda en `192.168.1.50`:

```powershell
New-NetFirewallRule `
  -DisplayName "AnIA Ollama LAN Solo Portatil" `
  -Direction Inbound `
  -Protocol TCP `
  -LocalPort 11434 `
  -RemoteAddress 192.168.1.50 `
  -Action Allow `
  -Profile Private
```

## Prueba Desde El Portatil

Cuando el portatil este instalado y conectado a la misma red:

```bash
curl http://192.168.1.133:11434/api/tags
```

Prueba de generacion:

```bash
curl http://192.168.1.133:11434/api/generate \
  -d '{
    "model": "llama3.1:8b",
    "prompt": "Responde solo con OK en espanol.",
    "stream": false
  }'
```

## Seguridad

- No exponer el puerto `11434` a internet.
- No abrir puertos en el router para Ollama.
- Permitir Ollama solo en la red local.
- Mas adelante, limitar el firewall a la IP fija del portatil.

## Estado De La Fase

- Ollama instalado: completado.
- Modelo `llama3.1:8b` instalado: completado.
- API local probada: completado.
- API por IP LAN probada: completado.
- Variable `OLLAMA_HOST` guardada: completado.
- Firewall Windows: pendiente de ejecutar como administrador.
