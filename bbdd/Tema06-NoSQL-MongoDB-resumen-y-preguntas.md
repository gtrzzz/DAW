# Tema 6. Bases de datos NoSQL y MongoDB

Contenido estudiado: desde el punto 1 hasta antes del punto 4 del PDF `Tema06. Teoria.pdf`.

## 1. Introduccion a NoSQL

Hay dos tipos principales de bases de datos:

- Relacionales: usan tablas, filas y columnas. Ejemplo: MySQL.
- No relacionales: no usan tablas rigidas. Tambien se llaman NoSQL.

MongoDB es una base de datos NoSQL. Sirve para guardar datos grandes, variados o que cambian mucho, como productos, usuarios, comentarios, registros o datos de redes sociales.

## MongoDB

MongoDB guarda la informacion en documentos. Un documento se parece a un objeto JSON:

```js
{
  "nombre": "Ana",
  "edad": 20,
  "activo": true
}
```

Comparacion sencilla:

| Base relacional | MongoDB |
|---|---|
| Base de datos | Base de datos |
| Tabla | Coleccion |
| Fila | Documento |
| Columna | Campo |

Una coleccion `usuarios` puede tener documentos como estos:

```js
{
  "nombre": "David",
  "edad": 25
}
```

```js
{
  "nombre": "Laura",
  "edad": 30
}
```

## MongoDB Compass

MongoDB Compass es una aplicacion visual para trabajar con MongoDB sin tener que escribir siempre comandos.

La conexion local habitual es:

```txt
mongodb://127.0.0.1:27017
```

Eso significa que MongoDB esta funcionando en tu propio ordenador.

## 2. Documentos y tipos de datos

MongoDB trabaja con documentos parecidos a JSON.

JSON usa pares clave-valor:

```js
{
  "clave": "valor"
}
```

Ejemplo:

```js
{
  "company_name": "Sparter",
  "founded_year": 2007,
  "twitter_username": null,
  "revenue": 879423000
}
```

## Tipos de datos JSON

JSON tiene tipos basicos:

- `String`: texto.
- `Number`: numero.
- `Boolean`: verdadero o falso.
- `Object`: objeto.
- `Array`: lista.
- `Null`: valor vacio.

Ejemplo:

```js
{
  "nombre": "Ana",
  "edad": 20,
  "activo": true,
  "telefono": null
}
```

JSON no tiene tipo fecha real. Una fecha suele guardarse como texto:

```js
{
  "fecha": "2026-06-02"
}
```

## Tipos de datos de MongoDB

MongoDB usa BSON, que es parecido a JSON, pero con mas tipos.

### String

Texto:

```js
{
  "nombre": "Carlos"
}
```

### Numbers

Numeros. MongoDB admite varios tipos, como `int`, `long`, `double` y `decimal`.

```js
{
  "edad": 25,
  "precio": 19.99
}
```

### Boolean

Verdadero o falso:

```js
{
  "activo": true
}
```

### Null

Campo sin valor:

```js
{
  "telefono": null
}
```

### Object

Un documento dentro de otro documento. Se llama documento anidado:

```js
{
  "nombre": "Hotel Sol",
  "host": {
    "host_id": "123",
    "host_name": "David"
  }
}
```

Para acceder a `host_name`:

```js
listing.host.host_name
```

### Array

Una lista de valores:

```js
{
  "generos": ["Drama", "Accion", "Comedia"]
}
```

Los arrays empiezan en indice `0`:

```js
generos[0] // Drama
generos[1] // Accion
```

### ObjectId

Cada documento tiene un campo `_id` unico. Funciona como clave primaria.

Si no escribes `_id`, MongoDB lo crea automaticamente:

```js
{
  "_id": ObjectId("..."),
  "nombre": "Ana"
}
```

### Date

MongoDB si puede guardar fechas reales:

```js
{
  "fecha": new Date()
}
```

## 3. Bases de datos en MongoDB

Comandos basicos de consola:

### Ver bases de datos

```js
show dbs
```

### Ver base de datos actual

```js
db
```

### Usar o preparar una base de datos

```js
use tienda
```

### Ver colecciones

```js
show collections
```

### Crear coleccion

```js
db.createCollection("productos")
```

### Eliminar coleccion

```js
db.productos.drop()
```

### Eliminar base de datos actual

```js
db.dropDatabase()
```

## Consulta de documentos

### `find()`

Muestra todos los documentos:

```js
db.comments.find()
```

Con filtro:

```js
db.comments.find({ "name": "Lauren Carr" })
```

### `pretty()`

Muestra el resultado mas ordenado:

```js
db.comments.find({ "name": "Lauren Carr" }).pretty()
```

### `findOne()`

Devuelve solo el primer documento:

```js
db.comments.findOne()
```

### Projection

Sirve para elegir que campos se muestran.

- `1`: mostrar.
- `0`: ocultar.

```js
db.comments.find(
  { "name": "Lauren Carr" },
  { "name": 1, "date": 1, "_id": 0 }
)
```

### `distinct()`

Devuelve valores unicos:

```js
db.movies.distinct("rated")
```

### `countDocuments()`

Cuenta documentos:

```js
db.movies.countDocuments()
```

Con condicion:

```js
db.movies.countDocuments({ "year": 1999 })
```

## Operadores condicionales

### Igualdad

```js
db.movies.find({ "year": 1999 })
db.movies.find({ "year": { $eq: 1999 } })
```

### Distinto

```js
db.movies.find({ "year": { $ne: 1999 } })
```

### Mayor que y mayor o igual

```js
db.movies.find({ "year": { $gt: 2000 } })
db.movies.find({ "year": { $gte: 2000 } })
```

### Menor que y menor o igual

```js
db.movies.find({ "year": { $lt: 2000 } })
db.movies.find({ "year": { $lte: 2000 } })
```

### Esta dentro de una lista

```js
db.movies.find({ "rated": { $in: ["G", "PG", "PG-13"] } })
```

### No esta dentro de una lista

```js
db.movies.find({ "rated": { $nin: ["G", "PG"] } })
```

## Operadores logicos

### `$and`

Todas las condiciones deben cumplirse:

```js
db.movies.find({
  $and: [
    { "rated": "UNRATED" },
    { "year": 2008 }
  ]
})
```

MongoDB tambien permite hacerlo directamente:

```js
db.movies.find({
  "rated": "UNRATED",
  "year": 2008
})
```

### `$or`

Debe cumplirse al menos una condicion:

```js
db.movies.find({
  $or: [
    { "rated": "G" },
    { "year": 2005 }
  ]
})
```

### `$not`

Niega una condicion:

```js
db.movies.find({
  "num_mflix_comments": {
    $not: { $gte: 5 }
  }
})
```

## Expresiones regulares

Sirven para buscar texto por patrones.

Contiene `Opera`:

```js
db.movies.find({
  "title": { $regex: "Opera" }
})
```

Empieza por `The`:

```js
db.movies.find({
  "title": { $regex: "^The" }
})
```

Sin distinguir mayusculas y minusculas:

```js
db.movies.find({
  "title": { $regex: "the", $options: "i" }
})
```

## Arrays

Buscar documentos cuyo array contenga un valor:

```js
db.movies.find({ "cast": "Charles Chaplin" })
```

Buscar un array exacto:

```js
db.movies.find({
  "languages": ["English", "German"]
})
```

### `$all`

Busca arrays que contengan todos los valores, sin importar el orden:

```js
db.movies.find({
  "languages": {
    $all: ["English", "French", "Cantonese"]
  }
})
```

### `$slice`

Limita cuantos elementos de un array se muestran:

```js
db.movies.find(
  { "title": "Youth Without Youth" },
  { "languages": { $slice: 3 } }
)
```

## Objetos anidados

Se puede buscar dentro de objetos anidados usando notacion de punto:

```js
db.listings.find({
  "host.host_name": "David"
})
```

## Limitar, saltar y ordenar

### `limit()`

```js
db.movies.find().limit(3)
```

### `skip()`

```js
db.movies.find().skip(2)
```

### `sort()`

Ascendente:

```js
db.movies.find().sort({ "title": 1 })
```

Descendente:

```js
db.movies.find().sort({ "title": -1 })
```

## Insertar documentos

Insertar uno:

```js
db.new_movies.insert({
  "_id": 1,
  "title": "Dunkirk"
})
```

Insertar varios:

```js
db.new_movies.insertMany([
  { "_id": 2, "title": "Baby Driver" },
  { "_id": 3, "title": "Logan" }
])
```

Si no pones `_id`, MongoDB lo crea automaticamente.

## Eliminar documentos

Eliminar uno:

```js
db.new_movies.deleteOne({ "_id": 1 })
```

Eliminar varios:

```js
db.new_movies.deleteMany({
  "title": { $regex: "^movie" }
})
```

Cuidado:

```js
db.new_movies.deleteMany({})
```

Esto elimina todos los documentos de la coleccion.

Buscar y eliminar:

```js
db.movies.findOneAndDelete({ "title": "Dunkirk" })
```

## Reemplazar y actualizar documentos

### `replaceOne()`

Reemplaza todo el documento, excepto `_id`:

```js
db.users.replaceOne(
  { "_id": 5 },
  { "name": "Margaery Baratheon", "email": "margaery@got.es" }
)
```

### `updateOne()`

Actualiza solo algunos campos:

```js
db.movies.updateOne(
  { "title": "Macbeth" },
  { $set: { "year": 2015 } }
)
```

## Operadores de actualizacion

### `$set`

Cambia o anade un campo:

```js
db.movies.updateOne(
  { "title": "Macbeth" },
  { $set: { "type": "movie" } }
)
```

### `$inc`

Incrementa o decrementa un numero:

```js
db.movies.updateOne(
  { "title": "Macbeth" },
  { $inc: { "views": 1 } }
)
```

### `$mul`

Multiplica un campo numerico:

```js
db.products.updateOne(
  { "name": "Book" },
  { $mul: { "price": 2 } }
)
```

### `$rename`

Cambia el nombre de un campo:

```js
db.movies.updateOne(
  { "title": "Macbeth" },
  { $rename: { "num_mflix_comments": "comments" } }
)
```

### `$currentDate`

Pone la fecha actual:

```js
db.movies.updateOne(
  { "title": "Macbeth" },
  { $currentDate: { "last_updated": true } }
)
```

### `$unset`

Elimina un campo:

```js
db.movies.updateOne(
  { "title": "Macbeth" },
  { $unset: { "comments": "" } }
)
```

## Operaciones con arrays

### `$push`

Anade un elemento a un array:

```js
db.movies.updateOne(
  { "_id": 111 },
  { $push: { "genre": "Drama" } }
)
```

### `$each`

Anade varios elementos:

```js
db.movies.updateOne(
  { "_id": 111 },
  { $push: { "genre": { $each: ["History", "Action"] } } }
)
```

### `$addToSet`

Anade un elemento solo si no existe ya:

```js
db.movies.updateOne(
  { "_id": 111 },
  { $addToSet: { "genre": "Action" } }
)
```

### `$pop`

Elimina el ultimo elemento:

```js
db.movies.updateOne(
  { "_id": 111 },
  { $pop: { "genre": 1 } }
)
```

Elimina el primer elemento:

```js
db.movies.updateOne(
  { "_id": 111 },
  { $pop: { "genre": -1 } }
)
```

### `$pullAll`

Elimina varios elementos concretos:

```js
db.movies.updateOne(
  { "_id": 111 },
  { $pullAll: { "genre": ["Action", "Crime"] } }
)
```

# Preguntas

Responde con el numero y tu respuesta.

## Tipo test

1. Que significa NoSQL?

A. Bases de datos que solo usan SQL  
B. Bases de datos no relacionales  
C. Bases de datos sin datos  
D. Bases de datos solo para imagenes

2. En MongoDB, una tabla equivale a:

A. Documento  
B. Campo  
C. Coleccion  
D. ObjectId

3. En MongoDB, una fila equivale a:

A. Coleccion  
B. Documento  
C. Base de datos  
D. Comando

4. Que formato usa MongoDB internamente?

A. XML  
B. CSV  
C. BSON  
D. HTML

5. Que campo funciona como clave primaria en MongoDB?

A. `id`  
B. `_id`  
C. `primary`  
D. `key`

6. Que hace `show dbs`?

A. Borra bases de datos  
B. Muestra las bases de datos  
C. Crea una coleccion  
D. Inserta documentos

7. Que hace `use tienda`?

A. Borra la base de datos tienda  
B. Selecciona o prepara la base de datos tienda  
C. Muestra todas las colecciones  
D. Inserta una tienda

8. Que metodo devuelve solo el primer documento encontrado?

A. `find()`  
B. `findOne()`  
C. `insertOne()`  
D. `showOne()`

9. En una proyeccion, que significa poner un campo a `1`?

A. Ocultarlo  
B. Borrarlo  
C. Mostrarlo  
D. Multiplicarlo

10. Que operador se usa para mayor que?

A. `$lt`  
B. `$gte`  
C. `$gt`  
D. `$ne`

11. Que operador se usa para buscar valores dentro de una lista?

A. `$in`  
B. `$inside`  
C. `$array`  
D. `$list`

12. Que operador logico exige que se cumplan todas las condiciones?

A. `$or`  
B. `$and`  
C. `$not`  
D. `$all`

13. Que operador sirve para busquedas por patrones de texto?

A. `$regex`  
B. `$textOnly`  
C. `$patternFind`  
D. `$like`

14. Que metodo elimina muchos documentos?

A. `deleteOne()`  
B. `deleteMany()`  
C. `removeOne()`  
D. `dropMany()`

15. Que operador anade un elemento a un array?

A. `$set`  
B. `$push`  
C. `$pop`  
D. `$inc`

## Preguntas cortas

16. Explica con tus palabras que es MongoDB.

17. Que diferencia hay entre una coleccion y un documento?

18. Que es JSON?

19. Que diferencia hay entre JSON y BSON?

20. Para que sirve MongoDB Compass?

21. Que pasa si insertas un documento sin `_id`?

22. Que peligro tiene ejecutar esto?

```js
db.users.deleteMany({})
```

23. Para que sirve `.pretty()`?

24. Que diferencia hay entre `replaceOne()` y `updateOne()`?

25. Que diferencia hay entre `$push` y `$addToSet`?

## Escribir sintaxis

26. Escribe el comando para ver las bases de datos.

27. Escribe el comando para usar una base de datos llamada `clase`.

28. Escribe el comando para ver las colecciones.

29. Crea una coleccion llamada `alumnos`.

30. Busca todos los documentos de la coleccion `alumnos`.

31. Busca alumnos cuyo nombre sea `Ana`.

32. Busca un solo alumno cualquiera.

33. Cuenta cuantos documentos hay en `alumnos`.

34. Inserta este alumno:

```js
{
  "nombre": "Luis",
  "edad": 20
}
```

35. Inserta varios alumnos: Ana de 19 anos y Pedro de 22 anos.

36. Busca alumnos con edad mayor que 18.

37. Busca alumnos con edad menor o igual que 20.

38. Busca alumnos cuyo nombre sea `Ana` o `Pedro` usando `$or`.

39. Muestra solo el campo `nombre` de los alumnos y oculta `_id`.

40. Ordena los alumnos por edad de mayor a menor.

41. Limita el resultado a 5 alumnos.

42. Actualiza a Luis para ponerle edad `21`.

43. Anade a Luis un campo nuevo llamado `activo` con valor `true`.

44. Incrementa la edad de Luis en `1`.

45. Elimina el alumno llamado `Pedro`.

46. Anade `MongoDB` al array `asignaturas` de Ana usando `$push`.

47. Anade `SQL` al array `asignaturas` de Ana solo si no existe ya.

48. Elimina el ultimo elemento del array `asignaturas` de Ana.

49. Busca documentos donde el campo `nombre` empiece por `A` usando `$regex`.

50. Busca documentos donde el campo `nombre` contenga `ana` sin distinguir mayusculas/minusculas.
