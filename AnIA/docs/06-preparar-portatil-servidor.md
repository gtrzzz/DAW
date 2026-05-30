# Preparar Portatil Servidor

Fecha: 30/05/2026

## Objetivo

Preparar el portatil con Linux Mint para que actue como servidor local de AnIA.

Este portatil ejecutara:

- Backend comun de AnIA.
- Bot de Telegram.
- Pagina web.
- Servicios de arranque automatico.

El PC principal seguira ejecutando Ollama.

## Estado Inicial

El portatil ya ha probado conexion con Ollama en el PC principal:

```bash
curl http://192.168.1.133:11434/api/tags
```

Resultado validado el 30/05/2026:

```text
La respuesta devolvio JSON con el modelo llama3.1:8b disponible.
```

Esto confirma que la red local funciona.

## Datos Del Portatil

Fecha de validacion: 30/05/2026

```text
IP del portatil: 192.168.1.138
Sistema actual: Linux Mint
```

Esta IP se usara para acceder por SSH y, mas adelante, para entrar a la web local de AnIA.

## Arquitectura De Esta Fase

```text
Portatil Linux Mint
  -> servicios AnIA
  -> llama a http://192.168.1.133:11434
  -> PC principal con Ollama
```

## Paso 1: Actualizar El Sistema

Ejecutar en el portatil:

```bash
sudo apt update
sudo apt upgrade -y
```

Reiniciar si el sistema lo pide:

```bash
sudo reboot
```

## Paso 2: Instalar Herramientas Base

```bash
sudo apt install -y curl git python3 python3-venv python3-pip openssh-server ufw net-tools
```

Uso de cada paquete:

- `curl`: probar APIs y servicios.
- `git`: descargar y actualizar el repositorio.
- `python3`: ejecutar backend y bot.
- `python3-venv`: crear entornos virtuales.
- `python3-pip`: instalar dependencias Python.
- `openssh-server`: administrar el portatil desde el PC principal.
- `ufw`: firewall sencillo.
- `net-tools`: herramientas de red como `ifconfig`.

## Paso 3: Activar SSH

```bash
sudo systemctl enable ssh
sudo systemctl start ssh
sudo systemctl status ssh
```

Comprobar IP del portatil:

```bash
hostname -I
```

Desde el PC principal se podra entrar asi:

```powershell
ssh usuario@IP_DEL_PORTATIL
```

Ejemplo:

```powershell
ssh zzz@192.168.1.50
```

## Paso 4: Configurar Firewall Del Portatil

Permitir SSH:

```bash
sudo ufw allow OpenSSH
```

Activar firewall:

```bash
sudo ufw enable
```

Ver estado:

```bash
sudo ufw status verbose
```

De momento no hace falta abrir puertos para Telegram si usamos polling.

Mas adelante, para la web local, abriremos HTTP:

```bash
sudo ufw allow 80/tcp
```

Y si usamos HTTPS:

```bash
sudo ufw allow 443/tcp
```

## Paso 5: Fijar IP Local

Recomendado hacerlo desde el router con DHCP estatico.

Valores recomendados:

```text
PC principal:      192.168.1.133
Portatil servidor: 192.168.1.138
```

Si no se fija la IP, puede cambiar al reiniciar y romper la configuracion.

## Paso 6: Crear Carpeta De Trabajo

```bash
mkdir -p ~/proyectos
cd ~/proyectos
```

Clonar el repositorio original:

```bash
git clone https://github.com/gtrzzz/DAW.git
```

Entrar al proyecto AnIA:

```bash
cd ~/proyectos/DAW/AnIA
```

Comprobar archivos:

```bash
ls
```

## Paso 7: Crear Entorno Python

```bash
cd ~/proyectos/DAW/AnIA
python3 -m venv .venv
source .venv/bin/activate
```

Actualizar `pip`:

```bash
python -m pip install --upgrade pip
```

Todavia no instalamos dependencias del backend porque se definiran en la Fase 3.

## Paso 8: Crear Archivo De Entorno Local

No subir este archivo a GitHub.

```bash
cp .env.example .env
nano .env
```

Contenido recomendado inicial:

```env
TELEGRAM_BOT_TOKEN=pendiente
OLLAMA_URL=http://192.168.1.133:11434
OLLAMA_MODEL=llama3.1:8b
```

Comprobar que `.env` esta ignorado por Git:

```bash
git status --short
```

No deberia aparecer `.env`.

## Paso 9: Probar Ollama Desde La Carpeta Del Proyecto

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

Resultado esperado:

```text
OK
```

## Paso 10: Probar Administracion Remota Desde Windows

Desde el PC principal:

```powershell
ssh usuario@IP_DEL_PORTATIL
```

Si funciona, ya podremos administrar el portatil sin tocarlo fisicamente.

## Checklist De Fase 2

- Sistema actualizado. Estado: completado.
- SSH instalado y activo. Estado: completado.
- Firewall activo con OpenSSH permitido. Estado: completado.
- IP del portatil identificada. Estado: completado con `192.168.1.138`.
- Repositorio `DAW` clonado. Estado: completado.
- Carpeta `AnIA` disponible en el portatil. Estado: completado.
- Entorno `.venv` creado. Estado: completado.
- Archivo `.env` local creado. Estado: completado.
- Conexion con Ollama probada. Estado: completado.
- Acceso SSH desde Windows probado. Estado: pendiente de confirmar si se ha probado desde Windows.

## Siguiente Fase

Cuando este checklist este completo, pasamos a la Fase 3:

```text
Crear backend comun con FastAPI para web y Telegram.
```
