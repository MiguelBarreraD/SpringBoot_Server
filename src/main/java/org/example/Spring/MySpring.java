package org.example.Spring;

import java.util.Map;
import java.util.HashMap;
import java.lang.reflect.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.*;
import org.example.runtime.GetMapping;
import org.example.runtime.PostMapping;
import org.example.runtime.RestController;
import java.net.URL;

/**
 * Clase para cargar componentes Spring mediante reflexión.
 */
public class MySpring {

    /** Constante con el nombre del paquete a escanear. */
    private static final String PACKAGE_NAME = "org/example/Controller";

    /** Mapa que almacena los métodos GET mapeados por URL. */
    public static Map<String, Method> GetMethod = new HashMap<>();

    /** Mapa que almacena los métodos POST mapeados por URL. */
    public static Map<String, Method> PostMethod = new HashMap<>();

    /**
     * Método para cargar todos los componentes anotados desde el paquete
     * configurado.
     * 
     * @throws ClassNotFoundException si no se encuentra la clase.
     */
    public static void loadComponents() throws ClassNotFoundException {

        // Carga el classloader y obtiene el paquete
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        URL packageURL = classLoader.getResource(PACKAGE_NAME);

        // Itera sobre los archivos del paquete
        String path = packageURL.getPath();
        File folder = new File(path);
        File[] files = folder.listFiles();

        for (File file : files) {

            // Obtiene el nombre de la clase
            String className = file.getName();

            // Verifica que sea un archivo .class
            if (className.endsWith(".class")) {

                // Imprime en consola el nombre
                System.out.println("Clase cargada: " + className);

                // Obtiene el nombre de clase del archivo
                className = PACKAGE_NAME + "/" + className.substring(0, className.length() - 6);

                // Carga la clase usando el classloader
                Class<?> clazz = Class.forName(className.replace("/", "."));

                // Verifica si tiene la anotación
                if (clazz.isAnnotationPresent(RestController.class)) {

                    // Carga los métodos si es un Controller
                    loadMethods(clazz);

                } else {
                    System.out.println("NO ESTA FUNCIONANDO LA ANOTACIÓN");
                }
            }
        }
    }

    /**
     * Método interno para cargar los métodos de una clase.
     * 
     * @param clase la clase de la cual extraer los métodos.
     */
    private static void loadMethods(Class<?> clase) {

        Method[] metodos = clase.getDeclaredMethods();

        for (Method metodo : metodos) {
            CheckAndSave(metodo);
        }
    }

    /**
     * Verifica una anotación en un método y lo guarda en el mapa correspondiente.
     * 
     * @param metodo el método a verificar.
     */
    public static void CheckAndSave(Method metodo) {

        // Verifica la anotación GET
        if (metodo.isAnnotationPresent(GetMapping.class)) {

            GetMapping getMapping = metodo.getAnnotation(GetMapping.class);

            System.out.println("ES UN MÉTODO GET: " + getMapping);
            System.out.println("EL NOMBRE DEL MÉTODO ES: " + metodo.getName());

            saveComponentMethods(getMapping.value(), metodo, "GET");

        }

        // Verifica la anotación POST
        if (metodo.isAnnotationPresent(PostMapping.class)) {

            PostMapping postMapping = metodo.getAnnotation(PostMapping.class);

            System.out.println("ES UN MÉTODO POST: " + postMapping);
            System.out.println("EL NOMBRE DEL MÉTODO ES: " + metodo.getName());

            saveComponentMethods(postMapping.value(), metodo, "POST");
        }
    }

    /**
     * Guarda un método en el mapa correspondiente según el verbo HTTP.
     * 
     * @param path   la URL del método.
     * @param method el método en sí.
     * @param type   el verbo HTTP - GET o POST.
     */
    private static void saveComponentMethods(String path, Method method, String type) {

        switch (type) {
            case "GET":
                System.out
                        .println("El path para el get fue: " + path + ", El nombre del método fue:" + method.getName());
                GetMethod.put(path, method);

            case "POST":
                System.out.println(
                        "El path para el post fue: " + path + ", El nombre del método fue:" + method.getName());
                PostMethod.put(path, method);

            default:
                break;
        }
    }

    public static Method search(String path, String httpMethod) {
        switch (httpMethod) {
            case "GET":
                return GetMethod.get(path);

            case "POST":
                return PostMethod.get(path);
            default:
                return null;
        }
    }
}
