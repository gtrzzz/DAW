# Git Y GitHub

Fecha: 30/05/2026

## Comandos Basicos

Ver estado:

```powershell
git status
```

Ver cambios:

```powershell
git diff
```

Anadir archivos:

```powershell
git add .
```

Crear commit:

```powershell
git commit -m "Inicializa estructura del proyecto AnIA"
```

Subir cambios:

```powershell
git push
```

## Crear Repositorio En GitHub Con GitHub CLI

Iniciar sesion:

```powershell
gh auth login
```

Si `gh` no se reconoce despues de instalarlo, cerrar y abrir la terminal. En esta maquina tambien se puede ejecutar temporalmente con la ruta completa:

```powershell
& "C:\Program Files\GitHub CLI\gh.exe" auth login
```

Crear repositorio remoto desde esta carpeta:

```powershell
gh repo create AnIA --private --source . --remote origin --push
```

Con ruta completa:

```powershell
& "C:\Program Files\GitHub CLI\gh.exe" repo create AnIA --private --source . --remote origin --push
```

Si lo quieres publico:

```powershell
gh repo create AnIA --public --source . --remote origin --push
```

## Regla Importante

No subir nunca `.env` real, tokens, claves ni contrasenas.
