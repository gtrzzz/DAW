# Resolucion detallada del examen de Informatica para la Ingenieria

Documento hecho a partir del modelo `Examen_final_C2_22-23 (4).pdf` y usando el temario como referencia. Incluye respuestas del test, explicacion y soluciones razonadas de los tres problemas.

## Respuestas del test

| Pregunta | Respuesta |
| --- | --- |
| 1 | B |
| 2 | D |
| 3 | B |
| 4 | A |
| 5 | D |

## Pregunta 1

Enunciado: indica que afirmacion relacionada con los buses del sistema es incorrecta.

Respuesta correcta: **B**.

Explicacion:

- El temario dice que hay tres buses del sistema: bus de datos, bus de direcciones y bus de control.
- La opcion B cambia `direcciones` por `perifericos`.
- Los perifericos existen, pero no son uno de los tres tipos de bus del sistema.

Truco: memoriza la regla `DDC`: Datos, Direcciones y Control.

## Pregunta 2

Enunciado: que frase es incorrecta.

Respuesta correcta: **D**.

Explicacion:

- A es correcta: los drivers indican al sistema operativo como debe funcionar un dispositivo.
- B es correcta: una funcion del sistema operativo es gestionar entrada/salida.
- C es correcta: una tarjeta de red puede conectar el ordenador a una red e Internet.
- D es falsa: los smartphones si tienen sistema operativo, como Android o iOS.

Truco: en el temario aparece explicitamente que los dispositivos moviles tienen sistemas operativos.

## Pregunta 3

Enunciado: cual afirmacion sobre sistemas operativos es falsa.

Respuesta correcta: **B**.

Explicacion:

- A es cierta: el sistema operativo oculta detalles de la maquina.
- C es cierta: administra recursos de la maquina.
- D es cierta: puede ser centralizado o distribuido.
- B es falsa porque describe hardware electronico binario, no software de sistema.

Truco: si una frase habla de componentes electronicos, niveles de tension o circuitos fisicos, normalmente habla de hardware, no de sistema operativo.

## Pregunta 4

Codigo relevante:

```python
def mifuncion():
    ventana.title(vartexto.get())

def mostrar():
    mostarinformacion = Label(ventana, text="este es mi mensaje")

boton = Button(ventana, text="mostrar", command=mifuncion)
```

Respuesta correcta: **A**.

Explicacion:

- El boton tiene `command=mifuncion`, por tanto al pulsarlo se ejecuta `mifuncion`.
- `mifuncion` hace `ventana.title(vartexto.get())`.
- `vartexto.get()` lee el texto escrito en el `Entry`.
- La funcion `mostrar()` no se ejecuta nunca, porque no esta asociada al boton.
- Por eso no aparece una nueva etiqueta; solo cambia el titulo de la ventana.

Truco: en `tkinter`, mira siempre que funcion esta en `command`. Las funciones que existen pero no estan enlazadas no hacen nada.

## Pregunta 5

Respuesta correcta: **D**.

Explicacion:

La pantalla muestra:

- Botones `Aceptar`, `Eliminar`, `Deshacer` en la columna izquierda.
- Etiquetas `Nombre`, `Apellidos` en la columna central.
- Cajas `Entry` a la derecha.
- Texto `Resultado` debajo, ocupando parte central/derecha.

La opcion D coloca:

```python
btnconfirmar.grid(row=0,column=0)
btnborrar.grid(row=1,column=0)
btndeshacer.grid(row=2,column=0)
lblnombre.grid(row=1,column=1)
lblapellidos.grid(row=2,column=1)
txtnombre.grid(row=1,column=2)
txtapellidos.grid(row=2,column=2)
lblresultado.grid(row=3,column=1,columnspan=2)
```

Eso coincide con la imagen.

Truco: para resolver preguntas de `grid`, dibuja una tabla mental: `row` baja hacia abajo y `column` avanza hacia la derecha.

## Datos comunes de los problemas

El examen trabaja con una matriz irregular. Cada fila tiene un nombre de sustrato y despues varios recuentos.

```python
matriz = [
    ['sustrato_1', 371, 284, 392, 258, 310],
    ['sustrato_2', 416, 417, 396],
    ['sustrato_3', 333, 401, 316, 261, 347, 235],
    ['sustrato_4', 385, 355, 364, 300],
    ['sustrato_5', 308, 394, 366, 260, 384],
    ['sustrato_3', 239, 254, 242, 263, 283, 330, 241, 329],
    ['sustrato_1', 302, 415, 252, 236, 240, 240, 309]
]

sustratos_validos = (
    'sustrato_1', 'sustrato_2', 'sustrato_3',
    'sustrato_4', 'sustrato_5', 'sustrato_6'
)
```

Idea importante del temario:

- Esto es una matriz irregular, porque no todas las filas tienen el mismo numero de columnas.
- Para obtener los recuentos de una fila se usa slicing: `fila[1:]`.
- La primera posicion, `fila[0]`, es el nombre del sustrato.

## Problema 1

### Que pide

Crear un menu con tres opciones:

- `(D)` dar de alta una fila nueva con nombre de sustrato y recuentos.
- `(M)` mostrar resumen de la ultima fila introducida.
- `(S)` salir.

Ademas:

- El menu se repite hasta escoger `S`.
- Se aceptan mayusculas y minusculas.
- Si la opcion no es valida, se muestra error.
- El nombre del sustrato debe ser uno de los seis validos.
- Los recuentos deben ser enteros mayores o iguales que cero.
- Se dejan de pedir recuentos cuando el usuario introduce `*`.
- Si se pulsa `M` antes de introducir una fila, se avisa.

### Razonamiento

Del temario usamos:

- `while` para repetir el menu.
- `if`, `elif`, `else` para decidir la opcion.
- `input()` para pedir datos.
- `.upper()` para aceptar mayusculas y minusculas.
- `in` para validar que el sustrato pertenece a la tupla de validos.
- `try/except` para controlar errores al convertir con `int()`.
- Listas y `append()` para crear la nueva fila.
- `sum()`, `len()` y `round()` para total, promedio y redondeo.

### Solucion propuesta

```python
matriz = [
    ['sustrato_1', 371, 284, 392, 258, 310],
    ['sustrato_2', 416, 417, 396],
    ['sustrato_3', 333, 401, 316, 261, 347, 235],
    ['sustrato_4', 385, 355, 364, 300],
    ['sustrato_5', 308, 394, 366, 260, 384],
    ['sustrato_3', 239, 254, 242, 263, 283, 330, 241, 329],
    ['sustrato_1', 302, 415, 252, 236, 240, 240, 309]
]

sustratos_validos = (
    'sustrato_1', 'sustrato_2', 'sustrato_3',
    'sustrato_4', 'sustrato_5', 'sustrato_6'
)

ultima_fila = []
opcion = ''

while opcion != 'S':
    print('(D)ar de alta un sustrato')
    print('(M)ostrar datos')
    print('(S)alir')
    opcion = input('Escoge una opcion: ').upper()

    if opcion == 'D':
        nombre = input('Nombre del sustrato: ')
        while nombre not in sustratos_validos:
            print('Sustrato no valido')
            nombre = input('Nombre del sustrato: ')

        nueva_fila = [nombre]
        dato = input('Recuento (* para terminar): ')

        while dato != '*':
            try:
                recuento = int(dato)
                if recuento >= 0:
                    nueva_fila.append(recuento)
                else:
                    print('El recuento debe ser mayor o igual que cero')
            except ValueError:
                print('El recuento debe ser un numero entero')

            dato = input('Recuento (* para terminar): ')

        matriz.append(nueva_fila)
        ultima_fila = nueva_fila
        print('Fila anadida correctamente')

    elif opcion == 'M':
        if not ultima_fila:
            print('Todavia no se ha introducido ninguna fila')
        else:
            recuentos = ultima_fila[1:]
            total = sum(recuentos)
            if len(recuentos) > 0:
                promedio = round(total / len(recuentos), 1)
            else:
                promedio = 0

            print('Sustrato:', ultima_fila[0])
            print('Total de ejemplares:', total)
            print('Promedio de ejemplares:', promedio)

    elif opcion == 'S':
        print('Fin del programa')

    else:
        print('Opcion no valida')
```

### Por que se hace asi

- `opcion = input(...).upper()` evita escribir dos condiciones para cada letra: `d` y `D` pasan a ser `D`.
- `while nombre not in sustratos_validos` obliga a repetir hasta recibir un nombre correcto.
- `try/except ValueError` evita que el programa se rompa si el usuario escribe texto en vez de numero.
- `nueva_fila = [nombre]` crea una fila igual al formato de la matriz original.
- `nueva_fila.append(recuento)` anade cada valor al final, como explica el tema de listas.
- `ultima_fila[1:]` obtiene solo los recuentos y deja fuera el nombre.
- `if not ultima_fila` aprovecha que una lista vacia equivale a `False`.

Truco: cuando una fila mezcla texto y numeros, nunca hagas `sum(fila)`, porque fallara al intentar sumar el nombre. Usa `sum(fila[1:])`.

## Problema 2

### Que pide

Crear dos funciones:

1. Una funcion que reciba el nombre de un sustrato y busque todas sus apariciones en la matriz. Debe retornar numero de veces que aparece, total de recolecciones y promedio.
2. Otra funcion que use la anterior para obtener esa informacion de los seis sustratos y retorne una cadena con toda la informacion.

Restricciones:

- No usar variables globales.
- No usar `input()` ni `print()` dentro de las funciones.

### Razonamiento

Para no usar variables globales, la matriz se pasa como parametro. Para no imprimir dentro de funciones, las funciones devuelven datos con `return` y el programa principal imprime.

### Solucion propuesta

```python
def resumen_sustrato(matriz, nombre_sustrato):
    apariciones = 0
    total = 0
    numero_recuentos = 0

    for fila in matriz:
        if fila[0] == nombre_sustrato:
            apariciones += 1
            recuentos = fila[1:]
            total += sum(recuentos)
            numero_recuentos += len(recuentos)

    if numero_recuentos > 0:
        promedio = total / numero_recuentos
    else:
        promedio = 0

    return apariciones, total, promedio


def resumen_todos_sustratos(matriz, sustratos_validos):
    texto = ''

    for sustrato in sustratos_validos:
        apariciones, total, promedio = resumen_sustrato(matriz, sustrato)
        texto += f'{sustrato}: apariciones={apariciones}, total={total}, promedio={round(promedio, 1)}\n'

    return texto


matriz = [
    ['sustrato_1', 371, 284, 392, 258, 310],
    ['sustrato_2', 416, 417, 396],
    ['sustrato_3', 333, 401, 316, 261, 347, 235],
    ['sustrato_4', 385, 355, 364, 300],
    ['sustrato_5', 308, 394, 366, 260, 384],
    ['sustrato_3', 239, 254, 242, 263, 283, 330, 241, 329],
    ['sustrato_1', 302, 415, 252, 236, 240, 240, 309]
]

sustratos_validos = (
    'sustrato_1', 'sustrato_2', 'sustrato_3',
    'sustrato_4', 'sustrato_5', 'sustrato_6'
)

nombre = input('Introduce el sustrato a buscar: ')
apariciones, total, promedio = resumen_sustrato(matriz, nombre)

print('Resumen del sustrato pedido')
print('Apariciones:', apariciones)
print('Total:', total)
print('Promedio:', round(promedio, 1))

print('Resumen de todos los sustratos')
print(resumen_todos_sustratos(matriz, sustratos_validos))
```

### Resultado para la matriz del enunciado

Calculos principales:

- `sustrato_1` aparece 2 veces. Total: 3609. Promedio: 300.8.
- `sustrato_2` aparece 1 vez. Total: 1229. Promedio: 409.7.
- `sustrato_3` aparece 2 veces. Total: 4074. Promedio: 291.0.
- `sustrato_4` aparece 1 vez. Total: 1404. Promedio: 351.0.
- `sustrato_5` aparece 1 vez. Total: 1712. Promedio: 342.4.
- `sustrato_6` aparece 0 veces. Total: 0. Promedio: 0.

### Por que se hace asi

- `for fila in matriz` recorre cada fila de la matriz, como en el tema de matrices.
- `fila[0]` contiene el nombre.
- `fila[1:]` contiene solo los recuentos.
- `apariciones += 1` cuenta cuantas filas corresponden al sustrato.
- `total += sum(recuentos)` acumula todos los ejemplares.
- `numero_recuentos += len(recuentos)` cuenta cuantos valores numericos hay.
- El promedio correcto es `total / numero_recuentos`, no `total / apariciones`, porque se pide promedio de recolecciones, no promedio por fila.

Truco: si el sustrato no aparece, `numero_recuentos` queda en cero. Hay que evitar dividir entre cero.

## Problema 3

### Que pide

Crear dos funciones:

1. Guardar la matriz en `c:\documentos\sustrato.txt`, separando elementos por `;`. La funcion debe retornar que sustratos no aparecen o `Lecturas completas` si aparecen todos.
2. Anadir una linea mas al final del fichero usando una lista llamada `nuevaLinea`.

Restricciones:

- No usar variables globales.
- No usar `input()` ni `print()` dentro de las funciones.

### Razonamiento

Del tema de ficheros:

- Se usa `open()` con modo `'w'` para crear/sobrescribir el archivo.
- Se usa modo `'a'` para anadir al final.
- `with` cierra el fichero automaticamente.
- Como los ficheros de texto solo escriben texto, hay que convertir cada elemento con `str()`.

### Solucion propuesta

```python
def guardar_matriz(matriz, sustratos_validos):
    ruta = r'c:\documentos\sustrato.txt'

    with open(ruta, 'w') as fichero:
        for fila in matriz:
            linea = ';'.join(str(elemento) for elemento in fila)
            fichero.write(linea + '\n')

    no_aparecen = []
    for sustrato in sustratos_validos:
        encontrado = False
        for fila in matriz:
            if fila[0] == sustrato:
                encontrado = True

        if not encontrado:
            no_aparecen.append(sustrato)

    if no_aparecen:
        return ', '.join(no_aparecen)

    return 'Lecturas completas'


def anadir_linea(nueva_linea):
    ruta = r'c:\documentos\sustrato.txt'

    with open(ruta, 'a') as fichero:
        linea = ';'.join(str(elemento) for elemento in nueva_linea)
        fichero.write(linea + '\n')


matriz = [
    ['sustrato_1', 371, 284, 392, 258, 310],
    ['sustrato_2', 416, 417, 396],
    ['sustrato_3', 333, 401, 316, 261, 347, 235],
    ['sustrato_4', 385, 355, 364, 300],
    ['sustrato_5', 308, 394, 366, 260, 384],
    ['sustrato_3', 239, 254, 242, 263, 283, 330, 241, 329],
    ['sustrato_1', 302, 415, 252, 236, 240, 240, 309]
]

sustratos_validos = (
    'sustrato_1', 'sustrato_2', 'sustrato_3',
    'sustrato_4', 'sustrato_5', 'sustrato_6'
)

nuevaLinea = ['sustrato_5', 308, 394, 366, 260, 384]

resultado = guardar_matriz(matriz, sustratos_validos)
print(resultado)

anadir_linea(nuevaLinea)
```

### Que escribiria el fichero

El fichero quedaria con lineas parecidas a estas:

```text
sustrato_1;371;284;392;258;310
sustrato_2;416;417;396
sustrato_3;333;401;316;261;347;235
sustrato_4;385;355;364;300
sustrato_5;308;394;366;260;384
sustrato_3;239;254;242;263;283;330;241;329
sustrato_1;302;415;252;236;240;240;309
sustrato_5;308;394;366;260;384
```

### Resultado que retorna la primera funcion

Para la matriz del enunciado, falta `sustrato_6`, asi que retorna:

```text
sustrato_6
```

Si apareciesen los seis sustratos, retornaria:

```text
Lecturas completas
```

### Por que se hace asi

- `';'.join(...)` une los elementos con punto y coma, justo como pide el enunciado.
- `str(elemento)` es obligatorio porque `join()` solo une cadenas.
- Modo `'w'` crea el fichero desde cero.
- Modo `'a'` anade al final sin borrar lo anterior.
- La funcion no imprime: devuelve el texto con `return`, cumpliendo la restriccion.

Truco: si en un examen piden escribir listas en fichero de texto, piensa en `join()` y en convertir todo con `str()`.

## Version alternativa mas compacta del problema 3

Esta version usa comprension de listas y conjuntos. Es mas corta, pero menos basica.

```python
def guardar_matriz(matriz, sustratos_validos):
    ruta = r'c:\documentos\sustrato.txt'

    with open(ruta, 'w') as fichero:
        for fila in matriz:
            fichero.write(';'.join(str(x) for x in fila) + '\n')

    encontrados = set(fila[0] for fila in matriz)
    faltan = [s for s in sustratos_validos if s not in encontrados]

    if faltan:
        return ', '.join(faltan)
    return 'Lecturas completas'
```

Recomendacion: para un examen introductorio, la version larga suele ser mejor porque se entiende mas facilmente y usa estructuras vistas en el temario.

## Errores tipicos que hay que evitar

- Usar `sum(fila)` cuando `fila[0]` es texto.
- Calcular el promedio dividiendo entre numero de filas en vez de numero de recuentos.
- Olvidar convertir el `input()` con `int()` o `float()`.
- No capturar `ValueError` cuando el usuario introduce algo no numerico.
- Usar variables globales dentro de funciones cuando el enunciado lo prohibe.
- Hacer `print()` dentro de funciones si el enunciado dice que no se puede mostrar informacion en ellas.
- Escribir `command=mifuncion()` en `tkinter` en vez de `command=mifuncion`.
- Abrir en modo `'w'` cuando se queria anadir al final; para anadir se usa `'a'`.
