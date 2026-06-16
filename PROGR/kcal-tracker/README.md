# Kcal Tracker

Aplicación local en Java para gestionar atletas sin depender de una hoja de cálculo. El programa replica los bloques principales de la plantilla original: cuestionario inicial, nutrición, mediciones corporales y valoración semanal.

## Módulos

### Cuestionario inicial

Recoge los datos de alta del atleta:

- Datos personales: nombre, fecha de nacimiento, sexo, trabajo, estado civil y experiencia previa.
- Datos físicos: altura, peso, porcentaje de grasa, VO2 max, lesiones, enfermedades y alergias.
- Medidas antropométricas iniciales: bíceps, pecho, cintura, cadera, muslo y gemelo.
- Objetivos: corporales, salud, rendimiento, funcionales y psicológicos.
- Día a día: pasos, horas de inactividad, trabajo, entrenamiento, sueño, calidad del sueño, estrés, comidas, dieta y medicación.
- Observaciones generales.

### Nutrición

Calcula automáticamente:

- Metabolismo en reposo con la fórmula Mifflin-St Jeor.
- Gasto calórico total según nivel de actividad.
- Calorías objetivo según contexto: pérdida de grasa, ganancia muscular o mantenimiento.
- Reparto de gramos de proteínas, grasas e hidratos de carbono.

Factores usados:

- Sedentario: 1.2
- Ligeramente activo: 1.5
- Activo: 1.7
- Muy activo: 2.0

### Mediciones

Permite registrar mediciones periódicas del atleta:

- Fecha.
- Peso.
- Porcentaje de grasa corporal.
- Bíceps, pecho, cintura, cadera, muslo y gemelo.
- Observaciones.

### Valoración semanal

Permite registrar seguimiento semanal dividido en:

- Entrenamiento: sesiones completadas, cansancio y recuperación.
- Nutrición: peso en ayunas, hambre, saciedad, hidratación y seguimiento del plan.
- Descanso: horas de sueño, calidad del sueño, estrés y motivación.
- Respuestas cualitativas: logros, mejoras, comentarios de entrenamiento, nutrición y feedback para el entrenador.

Los campos de mesociclo y microciclo ya existen como texto simple para dejar preparada la ampliación futura.

## Arquitectura

La aplicación es sencilla y local:

- `App.java`: punto de entrada.
- `ServidorLocal.java`: backend HTTP local y frontend HTML generado desde Java.
- `RepositorioAtletas.java`: persistencia local en `datos/atletas.bin`.
- `Atleta.java`: entidad principal.
- `Objetivos.java`, `DatosDiaADia.java`, `MedicionCorporal.java`, `ValoracionSemanal.java`: datos del atleta.
- `CalculadoraNutricion.java`: cálculos nutricionales.
- `ResultadoNutricion.java`: resultado del cálculo.
- `Formulario.java`: lectura de formularios HTML.

No usa frameworks ni dependencias externas.

## Ejecución

Desde VS Code puedes ejecutar `App.java` con el soporte de Java.

El proyecto tiene configurado un JDK local en `.vscode/settings.json`. Si una terminal estaba abierta antes de la configuración, ciérrala y abre una nueva para que reconozca `javac` y `java` desde el `PATH`.

Si tienes un JDK configurado en PATH:

```powershell
javac -encoding UTF-8 -d bin src\*.java
java -cp bin App
```

Después abre:

```text
http://localhost:8080
```

En la pantalla principal puedes abrir clientes registrados por su ID.

## Persistencia

Los atletas se guardan en:

```text
datos/atletas.bin
```

Si quieres empezar desde cero, cierra la aplicación y elimina ese archivo.

## Pendientes naturales

- Gráficas de evolución de mediciones.
- Exportación a CSV o Excel.
- Edición y borrado de atletas, mediciones y valoraciones.
- Módulo completo de mesociclos y microciclos.
- Separar frontend estático si el proyecto crece.
