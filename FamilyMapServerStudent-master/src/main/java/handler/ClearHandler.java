package handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import service.ClearService;
import results.ClearResult;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;

/**
 * A handler for the clear API
 */
public class ClearHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            // Confirm the Request Method is a POST Method
            if (exchange.getRequestMethod().toUpperCase().equals("POST")) {

                // Execute clear logic via ClearService object
                ClearService service = new ClearService();
                ClearResult result = service.clear();

                // Send HTTP Response
                if (result.isSuccess()) {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                } else {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                }

                // Serialize ClearResult to JSON String
                Gson gson = new Gson();
                Writer resBody = new OutputStreamWriter(exchange.getResponseBody());
                gson.toJson(result, resBody);
                resBody.close();

            } else {
                // The Request Method was not of type POST
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_METHOD, 0);
                // Close the output stream for the response body
                exchange.getResponseBody().close();
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
