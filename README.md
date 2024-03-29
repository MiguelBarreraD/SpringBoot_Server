# TALLER DE ARQUITECTURAS DE SERVIDORES DE APLICACIONES, META PROTOCOLOS DE OBJETOS, PATRÓN IOC, REFLEXIÓN

## RETO 
Para este taller los estudiantes deberán construir un servidor Web (tipo Apache) en Java. El servidor debe ser capaz de entregar páginas html e imágenes tipo PNG. Igualmente el servidor debe proveer un framework IoC para la construcción de aplicaciones web a partir de POJOS. Usando el servidor se debe construir una aplicación Web de ejemplo. El servidor debe atender múltiples solicitudes no concurrentes.

Para este taller desarrolle un prototipo mínimo que demuestre capcidades reflexivas de JAVA y permita por lo menos cargar un bean (POJO) y derivar una aplicación Web a partir de él. 

Debe entregar su trabajo al final del laboratorio. Luego puede complementar para entregarlo en 8 días. Se verificara y compararán el commit del día de inicio del laboratorio y el dela entrega final.

## Diseño
#### Construcción Notaciones:

Anotación para clases que van a funcionar como controladores REST 
```
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public @interface RestController {

    }
```

Anotación para clases que van a funcionar para los métodos GET de la API
```
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface GetMapping {

        public String value();
    }
```
Anotación para clases que van a funcionar para los métodos POST de la API
```
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface PostMapping {

        public String value();
    }
```
#### Uso Notaciones
Consume la clase MovieAPI, la cual es la encargada de hacer peticiones al servidor que obtiene películas.
```
    @RestController
    public class MovieController {

    @GetMapping(value = "/movie")
    public static String getMovie(String nameMovie) {
        JsonObject response = null;
        try {
            response = MovieAPI.getMovie(nameMovie);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response.toString();
    }

    @PostMapping(value = "/Addmovie")
    public static String postMovie() {
        return "{\"mensaje\": \"Se añadió el nombre\"}";
    }

}
```
#### MySpring:
- loadComponents(): Método que carga dinámicamente las clases anotadas con @RestController en el paquete especificado (org.example.Controller). Utiliza reflexión para identificar y cargar las clases.
- loadMethods(Class<?> clase): Método interno que carga los métodos de una clase anotada con @RestController.
- CheckAndSave(Method metodo): Método que verifica si un método tiene las anotaciones @GetMapping o @PostMapping y lo guarda en los mapas correspondientes.
- saveComponentMethods(String path, Method method, String type): Método que guarda un método en el mapa adecuado según el verbo HTTP.
- search(String path, String httpMethod): Método que busca un método en los mapas de acuerdo con la ruta y el verbo HTTP.

#### HttpServer:
- main(String[] args): Método principal que inicia el servidor HTTP.
- httpOkResponseHeader(URI requestUri): Genera el encabezado de una respuesta HTTP exitosa.
- httpOkResponseBody(URI requestUri): Genera el cuerpo de una respuesta HTTP exitosa (utiliza el método convertImageToBytes para imágenes).
- convertImageToBytes(Path imagePath): Convierte una imagen en bytes.
- httpErrorResponseHeader(): Devuelve el encabezado de una respuesta HTTP de error.
- httpErrorResponseBody(): Devuelve el cuerpo de una respuesta HTTP de error.
- getContentType(String filePath): Devuelve el tipo de contenido según la extensión del archivo.
- La clase utiliza la anotación @RestController y carga dinámicamente los controladores - utilizando el framework MySpring. Maneja las solicitudes HTTP GET y POST, invocando los - métodos correspondientes basándose en la URL y el verbo HTTP. Si no encuentra un método correspondiente, devuelve una respuesta de error.

## Instrucciones de uso 

- Para hacer uso del programa son necesarias tener instalado lo siguiente:
    - Java (JKD)
    - maven 

1. Abra su consola de comandos y clone el repositorio 
```
$ git clone https://github.com/MiguelBarreraD/SpringBoot_Server.git
```
2. Ingrese a la carpeta del repositorio:
```
$ cd .\SpringBoot_Server\
```
3. Compile el poyecto:
```
$ mvn clean package compile
```
4. Una vez compilado el proyecto, ejecute el siguiente comando para iniciar el programa (asegúrese de estar dentro de la carpeta principal):
```
$ java -cp .\target\classes\ org.example.HttpServer
```
Aunque el comando anterior ejecuta el servidor sin errores aparentes, no opera de manera adecuada. No obstante, al ejecutar directamente la clase HttpServer desde su entorno de desarrollo integrado (IDE), el servidor funciona correctamente.
5. Finalmente, acceda a la siguiente URL en su navegador para utilizar la aplicación:
```
→ http://localhost:35000/index.html
```
6. Dentro de la aplicación, encontrará la interfaz principal.
-   Ingrese el nombre de la película que desea consultar y seleccione el botón 'Search'.

![](img/Foto1.png)
7. A continuación podra ver toda la información de la pelicula consultada.

![](img/Foto3.png)

## Uso de petición GET

Una vez el programa este corriendo si desea usar la respuesta de la pelicula directamente siga los siguientes pasos:

```
http://localhost:35000/movie?name=(Nombre de la pelicula)
```
Por ejemplo:

```
http://localhost:35000/movie?name=Batman
```
La respuesta tendar que verse tal que así:

![](img/FotoEjemplo.png)

## Pruebas unitarias
Para realizar pruebas unitarias ejecute el siguiente comando:
```
$ mvn test
```
![](img/test.png)
### Autor
 - Miguel Angel Barrera Diaz
