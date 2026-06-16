# Documentación del trabajo realizado

## Objetivo

Se ha transformado el proyecto inicial de Java en una aplicación local para gestionar atletas y reducir la dependencia de una hoja de cálculo. La aplicación permite dar de alta clientes, consultar su información, calcular referencias nutricionales, registrar mediciones corporales y guardar valoraciones semanales.

## Referencias usadas

Se revisaron las cuatro imágenes de la carpeta `referencias/`, que representan las hojas principales de la plantilla original:

- Datos iniciales del atleta.
- Cálculo nutricional y reparto de macronutrientes.
- Mediciones corporales.
- Seguimiento o valoración semanal.

## Funcionalidades implementadas

- Servidor local Java accesible desde `http://localhost:8080`.
- Frontend HTML generado desde el backend, sin frameworks externos.
- Cuestionario inicial para registrar clientes.
- Listado de clientes registrados.
- Acceso directo a un cliente por su ID.
- Vista de detalle de cliente con su ID visible.
- Cálculo de nutrición con metabolismo basal, gasto total, calorías objetivo y gramos de macronutrientes.
- Registro de nuevas mediciones corporales.
- Registro de valoraciones semanales.
- Persistencia local en `datos/atletas.bin`.
- Documentación principal actualizada en `README.md`.
- Configuración de Java/JDK para compilar con `javac`.

## Cambios en código

- `App.java`: ahora arranca el servidor local.
- `ServidorLocal.java`: contiene las rutas HTTP, formularios y vistas HTML.
- `RepositorioAtletas.java`: gestiona guardado y lectura de atletas.
- `Formulario.java`: procesa formularios enviados desde el navegador.
- `CalculadoraNutricion.java`: calcula gasto energético y macronutrientes.
- `ResultadoNutricion.java`: representa el resultado de los cálculos nutricionales.
- `Atleta.java`: ampliado para ser persistible y guardar observaciones.
- `Objetivos.java`: agrupa objetivos y contexto del atleta.
- `DatosDiaADia.java`: agrupa hábitos diarios.
- `MedicionCorporal.java`: representa cada registro de mediciones.
- `ValoracionSemanal.java`: representa cada seguimiento semanal.
- `TipoDieta.java`: corregido el nombre del archivo para que coincida con el enum.

## Configuración añadida

- `.vscode/settings.json`: configurado para usar el JDK localizado en VS Code.
- `.gitignore`: excluye `bin/` y `datos/*.bin` para no subir compilados ni datos locales.

## Verificación

El proyecto se compiló correctamente con:

```powershell
javac -encoding UTF-8 -d bin src\*.java
```

La versión de Java usada para compilar fue:

```text
javac 21.0.10
```

## Ejecución

Desde la carpeta del proyecto:

```powershell
javac -encoding UTF-8 -d bin src\*.java
java -cp bin App
```

Después se abre en el navegador:

```text
http://localhost:8080
```

## Pendientes posibles

- Añadir edición y borrado de clientes.
- Añadir edición y borrado de mediciones y valoraciones.
- Crear gráficas de evolución.
- Exportar datos a CSV o Excel.
- Añadir módulo completo de mesociclos y microciclos.
