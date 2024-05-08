# 📋 Tarea Semanal: Proyecto E2E - Entrega 1

## Descripción 💡
Esta tarea implica la implementación inicial del proyecto E2E. Deben desarrollar las **entidades** correspondientes a la base de datos, la capa de **lógica de negocios** a través de los **servicios**, y la capa de **controladores** para manejar las peticiones HTTP y sus respuestas.


## Indicaciones

**IMPORTANTE** 🚨

- No se debe mover ni cambiar el nombre de ningún archivo, ya que esto podría ocasionar fallas en la calificación automática y resultar en una puntuación de 0.
- Se especifico el uso de `enum`s para ciertos atributos en algunas clases. A continuación se detallan estos `enum`s, cuya correcta implementación es crucial para evitar posibles fallos en las pruebas automatizadas.

| Nombre del `enum` | Es atributo de ... | Valores                                                |
|-------------------|--------------------|--------------------------------------------------------|
| Category          | `Driver`           | X, XL, BLACK                                           |
| Status            | `Ride`             | REQUESTED, ACCEPTED, IN_PROGRESS, COMPLETED, CANCELLED |
| Role              | `User`             | ADMIN, PASSENGER, DRIVER                               |

- La tabla intermedia `UserLocation`, que relaciona `Passenger` y `Coordinate`, utiliza una clave primaria compuesta, lo que significa que tiene dos claves primarias. JPA no puede implementar esto mediante anotaciones directas, por lo que se debe seguir el enfoque detallado en este [enlace](https://www.codejava.net/frameworks/spring/spring-data-jpa-composite-primary-key-examples). Esta peculiaridad técnica permite manejar tablas intermedias con atributos adicionales, mientras se aprende a sortear esta limitación de JPA.
- Aunque el uso de tablas intermedias con atributos adicionales no es inusual ni problemático a nivel de base de datos, puede causar un problema específico al serializar a JSON debido a la generación de referencias circulares. Para evitar este problema, utilicen la siguiente anotación en las clases de entidades mencionadas anteriormente: `@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")`.

### Entidades ⚙️ 

Las entidades requeridas fueron presentadas al final de la primera semana. Las entidades adicionales agregadas por los alumnos no serán consideradas en la calificación automática, pero se permite su inclusión si es necesario, siempre y cuando se discuta con los profesores.

### Endpoints 🌐

A continuación se detallan los endpoints que deben implementar por entidad y sus descripciones:

> Los `Query Parameters` deben de tener el mismo nombre que se indica en estas tablas. 

#### Recurso `Driver`

| Método HTTP | URI                   | Path Parameter | Query Parameter     | Request Body | Http Status    | Response Body |
|-------------|-----------------------|----------------|---------------------|--------------|----------------|---------------|
| GET         | /driver/{id}          | id             | -                   |              | 200 OK         | Driver        |
| POST        | /driver               |                | -                   | `Driver`     | 201 Created    |               |
| DELETE      | /driver/{id}          | id             | -                   |              | 204 No Content |               |
| PUT         | /driver/{id}          | id             | -                   | `Driver`     | 200 OK         |               |
| PATCH       | /driver/{id}/location | id             | latitude, longitude |              | 200 OK         |               |
| PATCH       | /driver/{id}/car      | id             | -                   | `Vehicle`    | 200 OK         |               |

#### Recurso `Passenger`

| Método HTTP | URI                                   | Path Parameter   | Query Parameter | Request Body | Http Status    | Response Body        |
|-------------|---------------------------------------|------------------|-----------------|--------------|----------------|----------------------|
| GET         | /passenger/{id}                       | id               | -               |              | 200 OK         | `Passenger`          |
| DELETE      | /passenger/{id}                       | id               | -               |              | 204 No Content |                      |
| PATCH       | /passenger/{id}                       | id               | description     | `Coordinate` | 200 OK         |                      |
| GET         | /passenger/{id}/places                | id               | -               |              | 200 OK         | `List\<Coordinate\>` |
| DELETE      | /passenger/{id}/places/{coordinateId} | id, coordinateId | -               |              | 204 No Content |                      |


#### Recurso `Review`

| Método HTTP | URI          | Path Parameter | Query Parameter | Request Body | Http Status | Response Body |
|-------------|--------------|----------------|-----------------|--------------|-------------|---------------|
| POST        | /review      | -              | rideId          | `Review`     | 200 (OK)    |               |
| DELETE      | /review/{id} | id (Long)      | -               | -            | 200 (OK)    |               |


#### Recurso `Ride`

| Método HTTP | URI                   | Path Parameter | Query Parameter | Request Body | Http Status    | Response Body  |
|-------------|-----------------------|----------------|-----------------|--------------|----------------|----------------|
| POST        | /ride                 | -              | -               | `Ride`       | 200 OK         |                |
| PATCH       | /ride/assign/{rideId} | rideId         | -               | -            | 200 OK         |                |
| DELETE      | /ride/{rideId}        | rideId         | -               | -            | 204 No Content |                |
| GET         | /ride/{userId}        | userId         | page, size      | -            | 200 OK         | `Page\<Ride\>` |


## 🛠️ Cómo ejecutar las pruebas localmente 🛠️

Para ejecutar pruebas localmente con Maven en un proyecto de Spring Boot, sigue estos pasos detallados:

1. Confirma que estás utilizando IntelliJ IDEA como tu entorno de desarrollo integrado (IDE).
2. Abre una terminal dentro de la raíz de tu proyecto Spring Boot.
3. Para iniciar todas las pruebas, ingresa el siguiente comando en la terminal y presiona `Ctrl + Shift + Enter` para ejecutarlo:

```
mvn clean test
```

Este comando ejecutará todas las pruebas unitarias y de integración definidas en el proyecto. Podrás ver los resultados de las pruebas en la salida de la terminal.

También puedes ejecutar pruebas individuales o un subconjunto de pruebas utilizando los siguientes comandos:

```
# Ejecutar una prueba individual
mvn clean test -Dtest=NombreDeLaPrueba

# Ejecutar un paquete de pruebas
mvn clean test -Dtest=com.example.package.**
```

**Importante** 🚨: Los códigos de estado mostrados son para casos de éxito. No se restrinjan a estos, ya que también se evaluarán los códigos de estado para casos de error.

¡Buena suerte con tus pruebas! 🚀