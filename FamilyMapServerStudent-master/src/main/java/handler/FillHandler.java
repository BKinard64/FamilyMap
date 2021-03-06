package handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import jsondata.FemaleNames;
import jsondata.LocationData;
import jsondata.MaleNames;
import jsondata.Surnames;
import service.FillService;
import requests.FillRequest;
import results.FillResult;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;

/**
 * A handler for the fill API
 */
public class FillHandler implements HttpHandler {
    private final LocationData locData;
    private final FemaleNames fmlNames;
    private final MaleNames mlNames;
    private final Surnames srNames;

    public FillHandler(LocationData locData, FemaleNames fmlNames, MaleNames mlNames, Surnames srNames) {
        this.locData = locData;
        this.fmlNames = fmlNames;
        this.mlNames = mlNames;
        this.srNames = srNames;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            // Confirm the Request Method is a POST Method
            if (exchange.getRequestMethod().toUpperCase().equals("POST")) {

                // Get the URL from the Request
                String[] url = exchange.getRequestURI().toString().split("/");

                // Confirm a username was sent as part of the Request
                if (url.length > 2) {

                    // Determine if optional generations parameter was sent with Request
                    String generations;
                    if (url.length > 3) {
                        generations = url[3];
                    } else {
                        generations = "4";
                    }

                    // Create FillRequest object to pass to FillService
                    FillRequest request = new FillRequest(url[2], generations);

                    // Execute FillRequest logic in FillService
                    FillService service = new FillService(locData, fmlNames, mlNames, srNames);
                    FillResult result = service.fill(request);

                    // Send HTTP Response
                    if (result.isSuccess()) {
                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                    } else {
                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                    }

                    // Serialize PersonResult to JSON String
                    Gson gson = new Gson();
                    Writer resBody = new OutputStreamWriter(exchange.getResponseBody());
                    gson.toJson(result, resBody);
                    resBody.close();

                } else {
                    // Username was not sent with Request
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                    // Close the output stream for the response body
                    exchange.getResponseBody().close();
                }
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
