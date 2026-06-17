# Resumen del temario de Informatica para la Ingenieria

Este resumen esta hecho a partir del temario `temario-informatica.pdf`. La asignatura combina conceptos basicos de informatica con programacion en Python, estructuras de datos, ficheros y una introduccion a interfaces graficas con `tkinter`.

## Tema 1. Conceptos basicos de informatica

La informatica es el tratamiento automatico de la informacion mediante ordenadores. Un ordenador recibe datos, los procesa y devuelve informacion util.

Conceptos clave:

- **Dato**: representacion simbolica de un hecho, cantidad o cualidad.
- **Informacion**: datos organizados que tienen significado para quien los recibe.
- **Bit**: unidad minima de informacion, puede valer `0` o `1`.
- **Byte**: normalmente 8 bits, suficiente para representar un caracter.

Un ordenador se compone de:

- **Hardware**: parte fisica, como CPU, memoria, placa base, perifericos y buses.
- **Software**: programas e instrucciones que hacen funcionar el hardware.

Arquitectura basica de Von Neumann:

- **CPU**: ejecuta instrucciones. Incluye unidad de control, ALU y reloj.
- **Memoria principal o RAM**: almacena programas y datos en ejecucion. Es rapida y volatil.
- **Entrada/salida**: comunica el ordenador con el exterior.
- **Bus del sistema**: comunica componentes internos.

Tipos de buses:

- **Bus de datos**: transporta informacion.
- **Bus de direcciones**: indica origen o destino de los datos.
- **Bus de control**: indica operaciones, como leer o escribir.

Truco de examen: si una respuesta cambia `direcciones` por `perifericos` como tipo de bus del sistema, suele ser falsa.

Otros elementos importantes:

- **Placa base**: conecta componentes internos.
- **Chipset**: controla el flujo de datos entre CPU, memoria y perifericos.
- **BIOS**: software de arranque que verifica componentes y busca el sistema operativo.
- **Drivers**: controladores que indican al sistema operativo como usar un hardware concreto.

Software:

- **Software propietario**: uso, modificacion o distribucion restringidos.
- **Software libre**: permite ejecutar, copiar, estudiar, modificar y redistribuir.
- **Software de sistema**: sistema operativo, drivers, diagnostico.
- **Software de programacion**: compiladores, interpretes, depuradores, IDE.
- **Software de aplicacion**: ofimatica, CAD, bases de datos, videojuegos, etc.

Sistemas operativos:

- Gestionan procesos, memoria, entrada/salida y abstraccion del hardware.
- Pueden ser monotarea/multitarea, monousuario/multiusuario, centralizados/distribuidos.
- Los moviles tambien tienen sistema operativo, como Android o iOS.

Bases de datos:

- Organizan informacion para recuperarla, analizarla y transmitirla.
- Un SGBD/DBMS actua como interfaz entre base de datos, usuario y aplicaciones.
- SQL permite consultar, insertar, actualizar y borrar datos.

## Tema 2. Variables, tipos y operadores

Una variable es un nombre asociado a una zona de memoria que contiene un valor.

Reglas habituales de nombres:

- Pueden contener letras, digitos y `_`.
- No pueden empezar por un digito.
- Python distingue mayusculas y minusculas: `x` y `X` son distintas.

Tipos basicos:

- `int`: enteros, por ejemplo `25`.
- `float`: decimales, por ejemplo `34.4`.
- `complex`: complejos, por ejemplo `1+3j`.
- `bool`: `True` o `False`.
- `str`: cadenas de caracteres.

Cadenas:

- Se escriben con comillas simples o dobles.
- Son inmutables.
- Se accede con indices: `texto[0]`, `texto[-1]`.
- Se obtienen segmentos con slicing: `texto[1:6]`.

Operadores:

- Asignacion: `=`, asignacion multiple `a, b = b, a`.
- Aritmeticos: `+`, `-`, `*`, `/`, `//`, `%`, `**`.
- Compuestos: `+=`, `-=`, `*=`, `/=`, `//=`, `%=`, `**=`.
- Relacionales: `<`, `<=`, `>`, `>=`, `==`, `!=`.
- Booleanos: `and`, `or`, `not`.

Precedencia importante:

1. Parentesis.
2. Potencias `**`.
3. Multiplicacion, division, division entera y modulo.
4. Suma y resta.
5. Comparaciones.
6. Logicos: `not`, `and`, `or`.

Truco: si dudas en una expresion, pon parentesis. En examen evita depender de recordar toda la precedencia.

## Tema 3. Funciones y modulos estandar

Una funcion agrupa instrucciones para realizar una tarea. Puede recibir argumentos y devolver un resultado.

Funciones basicas:

- `print()`: salida por pantalla.
- `input()`: entrada por teclado, siempre devuelve texto.
- `int()`, `float()`, `str()`: conversiones de tipo.
- `abs()`, `round()`: operaciones numericas.
- `len()`: longitud de una cadena o secuencia.
- `ord()`, `chr()`: conversion entre caracteres y codigo Unicode.

Ejemplo tipico:

```python
dato = input('Introduce el lado: ')
lado = float(dato)
area = lado * lado
print('El area es:', area)
```

Metodos de cadenas:

- `lower()`, `upper()`, `title()`.
- `isalpha()`, `isdigit()`, `isalnum()`, `isspace()`.
- `find()`: devuelve posicion o `-1`.
- `replace()`: devuelve una nueva cadena reemplazada.
- `split()`: trocea texto en lista.
- `strip()`, `lstrip()`, `rstrip()`: eliminan espacios.

Formato de cadenas:

```python
nombre = 'Ana'
nota = 7.456
print(f'{nombre} tiene un {round(nota, 1)}')
```

Modulos:

- `import math` permite usar `math.sqrt()`, `math.pi`, etc.
- `import random` permite generar valores aleatorios.
- Es mejor `import math` que `from math import *` para evitar colisiones de nombres.

## Tema 4. Control de flujo

Condicionales:

```python
if condicion:
    accion_1
elif otra_condicion:
    accion_2
else:
    accion_3
```

Se usan para bifurcar el programa segun condiciones.

Booleanos:

- `a and b` solo es `True` si ambos son `True`.
- `a or b` es `True` si al menos uno es `True`.
- `not a` invierte el valor.

Excepciones:

```python
try:
    numero = int(input('Numero: '))
except ValueError:
    print('No era un entero')
else:
    print('Correcto')
finally:
    print('Fin')
```

Excepciones frecuentes:

- `NameError`: nombre no definido.
- `TypeError`: tipo incorrecto.
- `ValueError`: valor no convertible o no valido.
- `ZeroDivisionError`: division por cero.
- `FileNotFoundError`: fichero no encontrado.

Bucle `while`:

```python
i = 1
while i <= 3:
    print(i)
    i += 1
```

Truco: en menus se suele usar `while opcion != 'S':` y convertir la opcion con `.upper()` para aceptar mayusculas y minusculas.

## Tema 5. Secuencias I

Una secuencia es una coleccion ordenada de elementos accesibles por indice.

Tipos principales:

- **Cadena**: secuencia inmutable de caracteres.
- **Lista**: secuencia mutable, entre corchetes.
- **Tupla**: secuencia inmutable, entre parentesis.

Operadores:

- `+`: concatena secuencias del mismo tipo.
- `*`: repite una secuencia.
- `in`: comprueba pertenencia.

Ejemplo:

```python
lista = [5, 7, 8]
if 7 in lista:
    print('Esta')
```

Bucle `for`:

```python
for elemento in lista:
    print(elemento)
```

`range()`:

- `range(5)` genera `0,1,2,3,4`.
- `range(1,5)` genera `1,2,3,4`.
- `range(0,5,2)` genera `0,2,4`.

`enumerate()` devuelve indice y valor:

```python
for i, letra in enumerate('ejemplo'):
    print(i, letra)
```

`break` corta un bucle y `continue` salta a la siguiente iteracion.

## Tema 6. Secuencias II

Slicing:

```python
secuencia[start:stop:step]
```

Reglas:

- Incluye `start`.
- No incluye `stop`.
- `step` indica el salto.
- Indices negativos cuentan desde el final.

Ejemplos:

```python
lista = ['a', 'b', 'c', 'd', 'e', 'f']
print(lista[2:4])    # ['c', 'd']
print(lista[-2:])    # ['e', 'f']
print(lista[::-1])   # lista invertida
```

Valor y referencia:

- En tipos simples, al modificar una variable no se cambia otra que tenia el mismo valor.
- En listas, dos variables pueden apuntar a la misma lista.
- Para copiar una lista se usa `copy()` o slicing `[:]`.

Funciones utiles:

- `len(secuencia)`.
- `min(secuencia)`.
- `max(secuencia)`.
- `sum(secuencia)`.
- `sorted(secuencia)`.

Metodos utiles de listas:

- `append(x)`: anade al final.
- `extend(iterable)`: anade varios elementos.
- `insert(i, x)`: inserta en posicion.
- `remove(x)`: elimina el primer valor igual a `x`.
- `pop(i)`: elimina y devuelve el elemento en posicion `i`; sin indice elimina el ultimo.
- `clear()`: vacia.
- `reverse()`: invierte in situ.
- `copy()`: copia superficial.
- `index(x)`: posicion de `x`; da error si no existe.
- `count(x)`: numero de apariciones.
- `sort()`: ordena in situ.

## Tema 7. Matrices

En Python, una matriz se representa como lista de listas.

```python
m = [[1, 3, 5], [7, 8, 0], [5, 6, 9]]
```

Acceso:

```python
print(m[0][1])  # fila 0, columna 1
m[1][2] = 10
```

Matrices irregulares:

```python
m = [[3, 5], [4, 3, 2]]
```

Aqui no todas las filas tienen la misma longitud.

Recorrido por filas:

```python
for i in range(len(m)):
    for j in range(len(m[i])):
        print(m[i][j])
```

Truco de examen: si la primera columna de cada fila es un nombre y el resto son numeros, se recorre desde `fila[1:]` para ignorar el nombre.

## Tema 8. Funciones y modulos propios

Definicion:

```python
def nombre_funcion(parametros):
    instrucciones
    return resultado
```

Ejemplo:

```python
def mayor(a, b):
    if a > b:
        return a
    return b
```

Buenas ideas:

- Una funcion debe tener una tarea clara.
- Los parametros modifican su comportamiento.
- `return` devuelve el resultado y termina la funcion.
- Si no hay `return`, devuelve `None`.

Paso por valor o referencia segun el temario:

- Tipos simples como enteros o cadenas se comportan como paso por valor: los cambios locales no afectan fuera.
- Tipos compuestos como listas permiten modificar el objeto original.

Ambito:

- Variables locales: definidas dentro de una funcion.
- Variables globales: definidas fuera.

Truco: en examenes suelen pedir no usar variables globales. Solucion: pasar la matriz como parametro y devolver resultados con `return`.

Modulos propios:

- Un modulo es un fichero `.py`.
- Al importarlo se ejecuta su codigo de nivel superior.
- Conviene poner funcionalidad reutilizable en funciones.

## Tema 9. Persistencia: ficheros de texto

Un fichero almacena datos de forma persistente. Los ficheros de texto guardan caracteres y se pueden leer con editores.

Protocolo basico:

1. Abrir.
2. Leer o escribir.
3. Cerrar.

Modos:

- `'r'`: lectura.
- `'w'`: escritura; sobrescribe si existe.
- `'a'`: adicion; anade al final.

Forma recomendada con `with`:

```python
with open('archivo.txt', 'w') as fichero:
    fichero.write('Hola\n')
```

Escritura:

- `write(cadena)` solo escribe texto.
- Para escribir numeros hay que convertirlos a cadena.
- Para saltos de linea se usa `\n`.

Lectura:

- `readline()`: lee una linea.
- `read(n)`: lee `n` caracteres.
- `readlines()`: devuelve lista de lineas.
- Tambien se puede iterar directamente:

```python
with open('archivo.txt', 'r') as fichero:
    for linea in fichero:
        print(linea)
```

Modulo `os`:

- Permite trabajar con rutas, comprobar existencia de archivos y carpetas, etc.

## Tema 10. Interfaz grafica con tkinter

Una IGU/GUI permite interactuar mediante ventanas, botones, campos de texto y otros widgets.

Importacion:

```python
from tkinter import *
```

Ventana basica:

```python
ventana = Tk()
ventana.geometry('500x100')
ventana.title('Mi primera IGU')
ventana.mainloop()
```

Widgets comunes:

- `Label`: texto.
- `Entry`: entrada de texto.
- `Button`: boton.
- `Checkbutton`: seleccion.
- `Listbox`: lista.
- `Frame`: contenedor.
- `LabelFrame`: contenedor con titulo.

Administradores de diseno:

- `pack()`: coloca widgets de forma relativa, por defecto de arriba abajo.
- `grid(row=..., column=...)`: coloca en una cuadrilla de filas y columnas.

Truco: no mezclar `pack()` y `grid()` en el mismo contenedor.

Eventos:

```python
def confirmar():
    print('Boton pulsado')

boton = Button(ventana, text='Confirmar', command=confirmar)
```

Atencion: en `command=confirmar` se pone el nombre de la funcion sin parentesis. Si se escribiera `command=confirmar()`, la funcion se ejecutaria al crear el boton.

Variables vinculadas:

- `StringVar`, `IntVar`, `BooleanVar`, `FloatVar` permiten conectar widgets con variables.

```python
texto = StringVar()
entrada = Entry(ventana, textvariable=texto)
print(texto.get())
texto.set('nuevo valor')
```

## Chuleta rapida para problemas de programacion

Validar opcion de menu:

```python
opcion = input('Opcion: ').upper()
if opcion not in ('D', 'M', 'S'):
    print('Opcion no valida')
```

Pedir enteros no negativos hasta `*`:

```python
valores = []
dato = input('Valor (* para terminar): ')
while dato != '*':
    try:
        numero = int(dato)
        if numero >= 0:
            valores.append(numero)
        else:
            print('Debe ser >= 0')
    except ValueError:
        print('Debe ser entero')
    dato = input('Valor (* para terminar): ')
```

Recorrer matriz donde la primera columna es nombre:

```python
for fila in matriz:
    nombre = fila[0]
    valores = fila[1:]
```

Calcular total y media:

```python
total = sum(valores)
media = total / len(valores) if valores else 0
```

Guardar matriz separada por `;`:

```python
with open(r'c:\documentos\sustrato.txt', 'w') as fichero:
    for fila in matriz:
        linea = ';'.join(str(x) for x in fila)
        fichero.write(linea + '\n')
```
