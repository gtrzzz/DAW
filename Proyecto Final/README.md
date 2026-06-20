# Plataforma IoT para Monitorizacion Ambiental

## 1. Introduccion

Este proyecto consiste en el diseno y desarrollo de una plataforma IoT para la monitorizacion ambiental mediante dispositivos basados en ESP32, sensores ambientales, una aplicacion web y piezas disenadas en 3D. La idea principal es crear un sistema capaz de recopilar datos del entorno, enviarlos a un servidor y mostrarlos en una interfaz web de forma clara, ordenada y, en la medida de lo posible, en tiempo real.

El proyecto nace de la union de varias areas de interes: robotica, electronica, diseno 3D y desarrollo de aplicaciones web. A nivel tecnico, el sistema combina microcontroladores, sensores, comunicacion por red, backend, base de datos, frontend y visualizacion de informacion. Esto permite crear un prototipo funcional con utilidad real y, al mismo tiempo, aplicar muchos de los conocimientos adquiridos durante el ciclo de Desarrollo de Aplicaciones Web.

La propuesta inicial contempla dos tipos de dispositivos: una estacion meteorologica inteligente y una boya inteligente. La estacion meteorologica estaria orientada a medir variables relacionadas con el aire y el entorno terrestre, como temperatura, humedad, presion atmosferica, calidad del aire o humedad del suelo. La boya inteligente estaria pensada para medir variables relacionadas con el agua, como temperatura, turbidez o nivel del agua.

El resultado esperado es un prototipo funcional que permita consultar los datos recogidos por los dispositivos desde una aplicacion web, visualizar graficas historicas, comprobar el estado de cada dispositivo y recibir alertas cuando algun valor supere determinados limites.

## 2. Motivacion

La motivacion principal de este proyecto es desarrollar una solucion tecnologica que combine hardware y software en un caso practico y visual. El uso de sensores ambientales permite trabajar con datos reales, mientras que la aplicacion web permite presentar esos datos de forma comprensible para el usuario.

El proyecto tambien resulta interesante porque permite acercarse al mundo del Internet de las Cosas, un campo cada vez mas presente en sistemas de domotica, agricultura inteligente, ciudades inteligentes, control ambiental y monitorizacion industrial. A traves de este trabajo se pretende construir una version simplificada de este tipo de sistemas, adaptada al alcance de un Trabajo de Fin de Grado y a un prototipo educativo.

Otro aspecto importante es el uso del diseno 3D. Las carcasas, soportes o estructuras de los dispositivos no se plantean solo como un elemento estetico, sino como una parte funcional del proyecto. Estas piezas pueden servir para proteger la electronica, organizar los sensores, facilitar el montaje y mejorar la presentacion final del prototipo.

## 3. Objetivo General

El objetivo general del proyecto es desarrollar una plataforma IoT de monitorizacion ambiental formada por dispositivos basados en ESP32 y una aplicacion web que permita visualizar, almacenar y analizar datos recogidos por sensores ambientales.

## 4. Objetivos Especificos

- Disenar y montar uno o varios dispositivos IoT basados en ESP32.
- Integrar sensores ambientales para medir variables como temperatura, humedad, turbidez, calidad del aire o humedad del suelo.
- Programar el ESP32 para leer datos de los sensores y enviarlos a un servidor mediante conexion WiFi.
- Desarrollar una API que reciba los datos enviados por los dispositivos.
- Almacenar las lecturas en una base de datos para poder consultar informacion historica.
- Crear una aplicacion web responsive para visualizar los datos actuales e historicos.
- Implementar comunicacion en tiempo real o actualizacion periodica de los datos mostrados en la web.
- Disenar graficas, tarjetas informativas y paneles de control para facilitar la interpretacion de los datos.
- Incorporar un sistema basico de alertas cuando determinados valores superen limites definidos.
- Disenar piezas o carcasas en 3D para integrar y proteger los componentes electronicos.
- Realizar pruebas de funcionamiento del sistema completo.

## 5. Alcance del Proyecto

El proyecto se plantea como un prototipo funcional, no como un producto comercial final. El alcance inicial se centrara en conseguir que al menos un dispositivo basado en ESP32 pueda leer datos de sensores reales, enviarlos al backend y mostrarlos correctamente en la aplicacion web.

La primera version del sistema podria estar formada por una estacion ambiental con sensores sencillos, como temperatura, humedad y presion atmosferica. Una vez completada esta base, se estudiaria la incorporacion de una boya inteligente para medir variables relacionadas con el agua, como temperatura o turbidez.

El sistema no pretende sustituir a estaciones meteorologicas profesionales ni a sistemas certificados de control de calidad del agua. Su objetivo es demostrar el funcionamiento de una arquitectura IoT completa, desde la captura del dato hasta su visualizacion en una aplicacion web.

## 6. Descripcion General del Sistema

El sistema estara formado por varios bloques principales:

- Dispositivos IoT basados en ESP32.
- Sensores ambientales conectados a cada ESP32.
- Conexion WiFi para enviar datos al servidor.
- Backend encargado de recibir, validar y guardar las lecturas.
- Base de datos para almacenar dispositivos, sensores y mediciones.
- Aplicacion web para visualizar los datos.
- Piezas o estructuras disenadas en 3D para alojar los componentes.

El flujo basico de funcionamiento sera el siguiente:

```text
Sensores -> ESP32 -> WiFi -> API Backend -> Base de datos -> Aplicacion web
```

Cada cierto intervalo de tiempo, el ESP32 leera los valores de los sensores conectados. Despues enviara esos datos al servidor mediante una peticion HTTP o mediante un protocolo de mensajeria como MQTT, dependiendo de la solucion final elegida. El backend recibira la informacion, comprobara que el formato sea correcto y la guardara en la base de datos. Finalmente, la aplicacion web mostrara los datos al usuario mediante paneles, tablas y graficas.

## 7. Posibles Dispositivos

### 7.1 Estacion Meteorologica Inteligente

La estacion meteorologica inteligente sera un dispositivo orientado a medir variables del entorno terrestre y atmosferico. Podria incluir sensores de temperatura, humedad ambiental, presion atmosferica, calidad del aire, luz ambiental y humedad del suelo.

Este dispositivo seria el mas adecuado para comenzar el desarrollo, ya que los sensores necesarios son relativamente sencillos de utilizar y no requieren trabajar con agua ni con problemas complejos de estanqueidad.

Posibles mediciones:

- Temperatura ambiental.
- Humedad ambiental.
- Presion atmosferica.
- Calidad del aire o CO2 aproximado.
- Luz ambiental.
- Humedad del suelo.

### 7.2 Boya Inteligente

La boya inteligente sera un prototipo orientado a medir variables relacionadas con el agua. Este dispositivo tendria una parte flotante o una estructura preparada para situar los sensores en contacto con el agua, manteniendo protegida la electronica principal.

La boya es una parte visualmente atractiva del proyecto, aunque tambien mas compleja. Por ese motivo, se plantea como una segunda fase o como un prototipo controlado, por ejemplo en un recipiente, acuario o deposito pequeno.

Posibles mediciones:

- Temperatura del agua.
- Turbidez del agua.
- Nivel del agua.
- Conductividad o pH, si el alcance del proyecto lo permite.

## 8. Tecnologias Previstas

### 8.1 Hardware

- ESP32 como microcontrolador principal.
- Protoboard o placa de conexiones.
- Cables Dupont.
- Fuente de alimentacion o bateria.
- Sensores ambientales.
- Carcasa o soporte disenado en 3D.

### 8.2 Sensores Iniciales

- BME280 para temperatura, humedad y presion.
- DHT22 como alternativa para temperatura y humedad.
- Sensor capacitivo de humedad del suelo.
- Sensor de turbidez para el modulo de agua.
- Sensor de luz, como LDR o BH1750.
- Sensor de calidad del aire, como MQ-135, teniendo en cuenta sus limitaciones de calibracion.

### 8.3 Backend

- Node.js.
- Express.
- API REST para recibir lecturas.
- Socket.IO o tecnologia similar para tiempo real, si se considera necesario.

### 8.4 Base de Datos

- MySQL o PostgreSQL como opcion relacional.
- MongoDB como alternativa documental.

La eleccion final dependera de los requisitos del proyecto y de las tecnologias que resulte mas conveniente utilizar durante el desarrollo.

### 8.5 Frontend

- HTML, CSS y JavaScript.
- React, Vue o una tecnologia similar si se decide usar un framework frontend.
- Librerias de graficas como Chart.js, ECharts o Recharts.
- Diseno responsive para permitir el uso en ordenador, tablet o movil.

### 8.6 Diseno 3D

- Tinkercad, FreeCAD, Fusion 360 o Blender.
- Impresion 3D para carcasas, soportes o estructuras.
- Diseno de piezas adaptadas a los sensores y al ESP32.

## 9. Requisitos Funcionales Provisionales

- El sistema debe permitir registrar uno o varios dispositivos IoT.
- El ESP32 debe poder leer datos de los sensores conectados.
- El ESP32 debe enviar las lecturas al backend mediante conexion WiFi.
- El backend debe recibir y validar las lecturas.
- El backend debe almacenar las lecturas en una base de datos.
- La aplicacion web debe mostrar la ultima lectura recibida de cada dispositivo.
- La aplicacion web debe mostrar graficas historicas de las mediciones.
- La aplicacion web debe indicar si un dispositivo esta activo o lleva tiempo sin enviar datos.
- El sistema debe permitir definir limites para generar alertas basicas.
- La aplicacion web debe ser responsive.

## 10. Requisitos No Funcionales Provisionales

- La aplicacion debe ser facil de utilizar y entender.
- La interfaz debe presentar los datos de forma clara y visual.
- El sistema debe estar organizado en modulos para facilitar su mantenimiento.
- El backend debe validar los datos recibidos antes de almacenarlos.
- La base de datos debe permitir consultar datos historicos de forma eficiente.
- El codigo debe estar documentado de forma suficiente.
- El prototipo fisico debe ser seguro y estar correctamente organizado.
- Las piezas 3D deben facilitar el montaje y la proteccion de los componentes.

## 11. Arquitectura Provisional

La arquitectura propuesta sigue una estructura cliente-servidor con dispositivos IoT conectados por red. Los ESP32 actuaran como clientes que recopilan informacion y la envian al servidor. El backend se encargara de centralizar los datos y la aplicacion web actuara como interfaz para el usuario.

```text
+----------------+        +----------------+        +----------------+
|    Sensores    | -----> |     ESP32      | -----> |  API Backend   |
+----------------+        +----------------+        +----------------+
                                                        |
                                                        v
                                               +----------------+
                                               | Base de datos  |
                                               +----------------+
                                                        |
                                                        v
                                               +----------------+
                                               | Aplicacion web |
                                               +----------------+
```

Esta arquitectura permite separar responsabilidades. El ESP32 se centra en medir y enviar datos. El backend se encarga de procesar y almacenar informacion. La aplicacion web se centra en la experiencia del usuario y en la visualizacion.

## 12. Modelo de Datos Provisional

De forma inicial, la base de datos podria incluir las siguientes entidades:

- Usuario: representa a la persona que accede a la plataforma.
- Dispositivo: representa cada ESP32 registrado en el sistema.
- Sensor: representa cada sensor asociado a un dispositivo.
- Lectura: representa cada medicion enviada por un sensor.
- Alerta: representa avisos generados cuando un valor supera un limite.

Ejemplo simplificado de lectura:

```json
{
  "deviceId": "meteo-001",
  "sensor": "temperature",
  "value": 24.6,
  "unit": "C",
  "timestamp": "2026-06-20T10:30:00Z"
}
```

## 13. Interfaz Web Prevista

La aplicacion web tendra como objetivo mostrar la informacion de forma clara. Algunas pantallas previstas son:

- Panel principal con resumen de todos los dispositivos.
- Vista detallada de cada dispositivo.
- Graficas historicas por sensor.
- Tabla de ultimas lecturas.
- Panel de alertas.
- Formulario de configuracion de dispositivos.

El dashboard podria mostrar tarjetas con la ultima temperatura, humedad, calidad del aire o turbidez. Tambien podria incluir colores para indicar estados normales, advertencias o valores criticos.

## 14. Plan de Desarrollo Provisional

### Fase 1: Investigacion y Preparacion

- Estudiar el funcionamiento basico del ESP32.
- Probar la conexion WiFi del ESP32.
- Elegir sensores iniciales.
- Definir la arquitectura del sistema.

### Fase 2: Primer Prototipo Hardware

- Conectar un sensor sencillo al ESP32.
- Leer datos desde el monitor serie.
- Comprobar estabilidad de las mediciones.

### Fase 3: Comunicacion con el Backend

- Crear una API basica.
- Enviar datos desde el ESP32 al servidor.
- Validar y guardar las lecturas recibidas.

### Fase 4: Aplicacion Web

- Crear el dashboard inicial.
- Mostrar las ultimas lecturas.
- Anadir graficas historicas.
- Mejorar el diseno responsive.

### Fase 5: Tiempo Real y Alertas

- Implementar actualizacion en tiempo real o por intervalos.
- Crear reglas de alerta.
- Mostrar avisos en la interfaz.

### Fase 6: Diseno 3D y Montaje Final

- Disenar carcasa o soporte.
- Imprimir y montar las piezas.
- Integrar sensores y ESP32.
- Preparar la demostracion final.

### Fase 7: Pruebas y Documentacion

- Probar el sistema completo.
- Documentar resultados.
- Anotar limitaciones.
- Proponer mejoras futuras.

## 15. Pruebas Previstas

Las pruebas se dividiran en varias categorias:

- Pruebas de sensores: comprobar que los sensores devuelven valores coherentes.
- Pruebas de comunicacion: comprobar que el ESP32 envia datos correctamente al backend.
- Pruebas de API: comprobar que los endpoints reciben, validan y almacenan informacion.
- Pruebas de base de datos: comprobar que las lecturas se guardan y consultan correctamente.
- Pruebas de interfaz: comprobar que los datos se muestran correctamente en la web.
- Pruebas responsive: comprobar el funcionamiento en distintos tamanos de pantalla.
- Pruebas del sistema completo: comprobar el flujo completo desde el sensor hasta la web.

## 16. Riesgos y Limitaciones

- Falta de experiencia inicial con ESP32.
- Posibles problemas de calibracion en sensores como calidad del aire, pH o turbidez.
- Dificultad para proteger correctamente la electronica en la boya.
- Dependencia de la conexion WiFi.
- Posibles errores en la lectura de sensores analogicos.
- Tiempo limitado para implementar todos los modulos propuestos.

Para reducir estos riesgos, el proyecto se desarrollara de forma incremental. Primero se implementara un sistema minimo funcional con un ESP32 y un sensor sencillo. Despues se anadiran nuevas funcionalidades de forma progresiva.

## 17. Mejoras Futuras

- Incorporar GPS para ubicar los dispositivos en un mapa.
- Anadir alimentacion mediante bateria y panel solar.
- Usar MQTT para una comunicacion IoT mas especializada.
- Implementar predicciones o analisis de tendencias.
- Crear una aplicacion movil o PWA.
- Mejorar la proteccion fisica de la boya.
- Anadir mas sensores y perfiles de dispositivo.
- Permitir exportar datos en CSV o PDF.
- Crear un sistema de usuarios con roles.

## 18. Conclusion Provisional

Este proyecto propone el desarrollo de una plataforma IoT completa que conecta dispositivos fisicos con una aplicacion web. La combinacion de ESP32, sensores, backend, base de datos, frontend y diseno 3D permite crear un trabajo amplio, practico y visual, adecuado para un Trabajo de Fin de Grado de Desarrollo de Aplicaciones Web.

Aunque algunas partes, como la boya inteligente o ciertos sensores avanzados, pueden presentar dificultades tecnicas, el proyecto puede adaptarse progresivamente. La prioridad sera conseguir una base funcional y estable: medir datos reales, enviarlos al servidor, almacenarlos y mostrarlos en la web. A partir de esa base, se podran anadir nuevas funcionalidades segun el tiempo disponible y los resultados obtenidos.

## 19. Bibliografia Inicial

- Documentacion oficial de ESP32: https://docs.espressif.com/
- Documentacion de Arduino: https://docs.arduino.cc/
- Documentacion de Node.js: https://nodejs.org/docs/
- Documentacion de Express: https://expressjs.com/
- Documentacion de Socket.IO: https://socket.io/docs/
- Documentacion de Chart.js: https://www.chartjs.org/docs/
- Guia arc42 para documentacion de arquitectura: https://docs.arc42.org/home/
- Guia Diataxis para documentacion tecnica: https://diataxis.fr/
