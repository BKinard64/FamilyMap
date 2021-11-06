package handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import service.LoadService;
import requests.LoadRequest;
import results.LoadResult;

import java.io.*;
import java.net.HttpURLConnection;

/**
 * A handler for the Load API
 */
public class LoadHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            // Confirm the Request Method is a POST Method
            if (exchange.getRequestMethod().toUpperCase().equals("POST")) {

                // Deserialize JSON String to Request Object
                Gson gson = new Gson();
                Reader reqBody = new InputStreamReader(exchange.getRequestBody());
                LoadRequest request = (LoadRequest)gson.fromJson(reqBody, LoadRequest.class);

                // Execute Load Logic via LoadService Object
                LoadService service = new LoadService();
                LoadResult result = service.load(request);

                // Send HTTP Response
                if (result.isSuccess()) {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                } else {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                }

                // Serialize LoadResult to JSON String
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
