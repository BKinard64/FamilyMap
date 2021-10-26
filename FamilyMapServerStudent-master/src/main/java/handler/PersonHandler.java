package handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import service.FamilyService;
import service.PersonService;
import service.requests.PersonRequest;
import service.results.FamilyResult;
import service.results.PersonResult;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;

public class PersonHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            // Confirm the Request Method is a GET Method
            if (exchange.getRequestMethod().toUpperCase().equals("GET")) {

                // Get the HTTP Request Headers
                Headers reqHeaders = exchange.getRequestHeaders();

                // Make sure an Authorization Key is present
                if (reqHeaders.containsKey("Authorization")) {

                    // Extract the AuthToken from the Header
                    String tokenString = reqHeaders.getFirst("Authorization");

                    // Get the URL path from the Request
                    String urlPath = exchange.getRequestURI().toString();

                    // If the path specifies a specific PersonID, call the PersonService
                    if (urlPath.length() > 8) {

                        // Extract the personID from the URL
                        String personID = urlPath.substring(8);

                        // Create PersonRequest object to pass to PersonService
                        PersonRequest pRequest = new PersonRequest(personID, tokenString);

                        // Execute PersonRequest logic in PersonService
                        PersonService pService = new PersonService();
                        PersonResult pResult = pService.person(pRequest);

                        // Send HTTP Response
                        if (pResult.isSuccess()) {
                            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                        } else {
                            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                        }

                        // Serialize PersonResult to JSON String
                        Gson gson = new Gson();
                        Writer resBody = new OutputStreamWriter(exchange.getResponseBody());
                        gson.toJson(pResult, resBody);
                        resBody.close();

                    } else {
                        // The path does not specify a PersonID, so call FamilyService instead

                        // Execute Request Logic in FamilyService
                        FamilyService fService = new FamilyService();
                        FamilyResult fResult = fService.family(tokenString);

                        // Send HTTP Response
                        if (fResult.isSuccess()) {
                            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                        } else {
                            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                        }

                        // Serialize FamilyResult to JSON String
                        Gson gson = new Gson();
                        Writer resBody = new OutputStreamWriter(exchange.getResponseBody());
                        gson.toJson(fResult, resBody);
                        resBody.close();
                    }

                } else {
                    // The Request Method does not provide an AuthToken
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_UNAUTHORIZED, 0);
                    // Close the output stream for the response body
                    exchange.getResponseBody().close();
                }
            } else {
                // The Request Method is not of type GET
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
