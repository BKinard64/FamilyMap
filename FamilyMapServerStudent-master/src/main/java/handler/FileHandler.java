package handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.nio.file.Files;

public class FileHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            // Confirm the Request Method is a GET Method
            if (exchange.getRequestMethod().toUpperCase().equals("GET")) {

                // Get the URL Path from the Request
                String urlPath = exchange.getRequestURI().toString();
                // If the URL Path is null or just a slash, convert the path to /index.html
                if (urlPath == null || urlPath.equals("/")) {
                    urlPath = "/index.html";
                }
                // Create relative file path from URL Path
                String filePath = "web" + urlPath;

                // Confirm the requested file exists
                File file = new File(filePath);
                if (file.exists()) {

                    // The file exists, so send a successful Response Header
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);

                    // Write the file to the response body output stream
                    OutputStream respBody = exchange.getResponseBody();
                    Files.copy(file.toPath(), respBody);

                    // Close the output stream
                    respBody.close();

                } else {

                    // The requested file does not exist
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_NOT_FOUND, 0);

                    // Send the 404.html file
                    File notFound = new File("web/HTML/404.html");
                    OutputStream respBody = exchange.getResponseBody();
                    Files.copy(notFound.toPath(), respBody);

                    // Close the output stream
                    respBody.close();

                }
            } else {
                // The Request Method was not of type GET
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_METHOD, 0);
            }
        } catch (IOException e) {
            // Report an internal server error
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_SERVER_ERROR, 0);
            // Close the output stream for the response body
            exchange.getResponseBody().close();
            // Log the stack trace
            e.printStackTrace();
        }
    }
}
