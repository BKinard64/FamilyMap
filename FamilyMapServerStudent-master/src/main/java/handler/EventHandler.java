package handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import service.EventService;
import service.FamilyEventsService;
import service.requests.EventRequest;
import service.results.EventResult;
import service.results.FamilyEventsResult;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;

public class EventHandler implements HttpHandler {
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

                    // If the path specifies a specific EventID, call the EventService
                    if (urlPath.length() > 7) {

                        // Extract the eventID from the URL
                        String eventID = urlPath.substring(7);

                        // Create an EventRequest Object to pass to EventService
                        EventRequest request = new EventRequest(eventID, tokenString);

                        // Execute EventRequest Logic in EventService
                        EventService service = new EventService();
                        EventResult eResult = service.event(request);

                        // Send HTTP Response
                        if (eResult.isSuccess()) {
                            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                        } else {
                            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                        }

                        // Serialize EventResult to JSON String
                        Gson gson = new Gson();
                        Writer resBody = new OutputStreamWriter(exchange.getResponseBody());
                        gson.toJson(eResult, resBody);
                        resBody.close();

                    } else {
                        // The path does not specify an EventID, so call FamilyEventsService instead

                        // Execute Request Logic in FamilyEventsService
                        FamilyEventsService feService = new FamilyEventsService();
                        FamilyEventsResult feResult = feService.familyEvents(tokenString);

                        // Send HTTP Response
                        if (feResult.isSuccess()) {
                            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                        } else {
                            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                        }

                        // Serialize FamilyResult to JSON String
                        Gson gson = new Gson();
                        Writer resBody = new OutputStreamWriter(exchange.getResponseBody());
                        gson.toJson(feResult, resBody);
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
