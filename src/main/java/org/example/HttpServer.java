package org.example;

import java.net.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

/**
 * Clase que implementa un servidor HTTP simple.
 */
public class HttpServer {

    /**
     * Método principal que inicia el servidor.
     *
     * @param args Argumentos de la línea de comandos.
     * @throws IOException  Si hay un problema de entrada/salida.
     * @throws URISyntaxException Si hay un problema con la URI.
     */
    public static void main(String[] args) throws IOException, URISyntaxException {
        ServerSocket serverSocket = null;

        // Intentar crear un socket del servidor en el puerto 35000.
        try {
            serverSocket = new ServerSocket(35000);
        } catch (IOException e) {
            System.err.println("No se puede escuchar en el puerto: 35000.");
            System.exit(1);
        }

        boolean running = true;

        // Esperar conexiones entrantes de clientes.
        while (running) {
            Socket clientSocket = null;
            try {
                System.out.println("Listo para recibir ...");
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                System.err.println("Error al aceptar la conexión.");
                System.exit(1);
            }

            // Configurar flujos de entrada/salida para la comunicación con el cliente.
            OutputStream out = clientSocket.getOutputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String inputLine;
            byte[] header, body;

            boolean firstLine = true;
            String uriStr = "";

            // Leer las líneas de la solicitud del cliente.
            while ((inputLine = in.readLine()) != null) {
                if (firstLine) {
                    uriStr = inputLine.split(" ")[1];
                    firstLine = false;
                }
                System.out.println("Recibido: " + inputLine);
                if (!in.ready()) {
                    break;
                }
            }

            // Parsear la URI de la solicitud.
            URI requestUri = new URI(uriStr);

            // Manejar la solicitud y enviar la respuesta al cliente.
            try {
                header = httpOkResponseHeader(requestUri);
                body = httpOkResponseBody(requestUri);
                out.write(header);
                out.write(body);
            } catch (Exception e) {
                header = httpErrorResponseHeader();
                body = httpErrorResponseBody();
                out.write(header);
                out.write(body);
            }

            // Cerrar flujos y el socket del cliente.
            out.close();
            in.close();
            clientSocket.close();
        }

        // Cerrar el socket del servidor.
        serverSocket.close();
    }

    /**
     * Método que genera el encabezado de una respuesta HTTP exitosa.
     *
     * @param requestUri URI de la solicitud.
     * @return Arreglo de bytes que representa el encabezado.
     * @throws IOException Si hay un problema de entrada/salida.
     */
    private static byte[] httpOkResponseHeader(URI requestUri) throws IOException {
        String contentType = getContentType(requestUri.getPath());
        String outputLine = "HTTP/1.1 200 OK\r\n"
                + "Content-Type: " + contentType + "\r\n"
                + "\r\n";
        return outputLine.getBytes();
    }

    /**
     * Método que genera el cuerpo de una respuesta HTTP exitosa.
     *
     * @param requestUri URI de la solicitud.
     * @return Arreglo de bytes que representa el cuerpo.
     * @throws IOException Si hay un problema de entrada/salida.
     */
    private static byte[] httpOkResponseBody(URI requestUri) throws IOException {
        Path file = Paths.get("target/classes/public" + requestUri.getPath());

        String contentType = getContentType(requestUri.getPath());
        if (contentType.equals("image/jpeg")) {
            byte[] imageBytes = convertImageToBytes(file);
            return imageBytes;
        } else {
            return Files.readAllBytes(Paths.get("target/classes/public" + requestUri.getPath()));
        }
    }

    /**
     * Método que convierte una imagen en bytes.
     *
     * @param imagePath Ruta de la imagen.
     * @return Arreglo de bytes que representa la imagen.
     * @throws IOException Si hay un problema de entrada/salida.
     */
    static byte[] convertImageToBytes(Path imagePath) throws IOException {
        BufferedImage image = ImageIO.read(imagePath.toFile());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", baos);
        return baos.toByteArray();
    }

    /**
     * Método que devuelve el encabezado de una respuesta HTTP de error.
     *
     * @return Arreglo de bytes que representa el encabezado de error.
     */
    private static byte[] httpErrorResponseHeader() {
        String outputLine = "HTTP/1.1 400 Not Found\r\n"
                + "Content-Type:text/html\r\n";
        return outputLine.getBytes();
    }

    /**
     * Método que devuelve el cuerpo de una respuesta HTTP de error.
     *
     * @return Arreglo de bytes que representa el cuerpo de error.
     */
    public static byte[] httpErrorResponseBody() {
        String outputLine = "\r\n"
                + "<!DOCTYPE html>\n"
                + "<html>\n"
                + "   <head>\n"
                + "       <title>Error No encontrado</title>\n"
                + "       <meta charset=\"UTF-8\">\n"
                + "       <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n"
                + "   </head>\n"
                + "   <body>\n"
                + "       <h1>Error</h1>\n"
                + "   </body>\n";
        return outputLine.getBytes();
    }

    /**
     * Método que devuelve el tipo de contenido según la extensión del archivo.
     *
     * @param filePath Ruta del archivo.
     * @return Tipo de contenido.
     */
    private static String getContentType(String filePath) {
        if (filePath.endsWith(".html")) {
            return "text/html";
        } else if (filePath.endsWith(".css")) {
            return "text/css";
        } else if (filePath.endsWith(".js")) {
            return "application/javascript";
        } else if (filePath.endsWith(".jpg")) {
            return "image/jpeg";
        } else {
            return "application/octet-stream";
        }
    }
}
