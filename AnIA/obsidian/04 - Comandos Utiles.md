# Comandos Utiles

Fecha: 30/05/2026

## Git

```powershell
git status
git add .
git commit -m "Mensaje del commit"
git push
```

## GitHub CLI

```powershell
gh auth login
gh repo create AnIA --private --source . --remote origin --push
```

## Ollama

```powershell
ollama list
ollama pull llama3.1:8b
curl http://192.168.1.133:11434/api/tags
```

## Ollama En Red Local

```powershell
setx OLLAMA_HOST "0.0.0.0:11434"
$env:OLLAMA_HOST = "0.0.0.0:11434"
Get-Process -Name ollama -ErrorAction SilentlyContinue | Stop-Process
Start-Process -FilePath "$env:LOCALAPPDATA\Programs\Ollama\ollama.exe" -ArgumentList "serve" -WindowStyle Hidden
netstat -ano | findstr ":11434"
```

## Firewall Como Administrador

```powershell
New-NetFirewallRule -DisplayName "AnIA Ollama LAN" -Direction Inbound -Protocol TCP -LocalPort 11434 -Action Allow -Profile Private
```
