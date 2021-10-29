package handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import jsondata.FemaleNames;
import jsondata.LocationData;
import jsondata.MaleNames;
import jsondata.Surnames;
import service.RegisterService;
import service.requests.RegisterRequest;
import service.results.RegisterResult;

import java.io.*;
import java.net.HttpURLConnection;

/**
 * A handler for the register API
 */
public class RegisterHandler implements HttpHandler {
    private final LocationData locData;
    private final FemaleNames fmlNames;
    private final MaleNames mlNames;
    private final Surnames srNames;

    public RegisterHandler(LocationData locData, FemaleNames fmlNames, MaleNames mlNames, Surnames srNames) {
        this.locData = locData;
        this.fmlNames = fmlNames;
        this.mlNames = mlNames;
        this.srNames = srNames;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            if (exchange.getRequestMethod().toUpperCase().equals("POST")) {

                // Deserialize JSON String to Request Object
                Gson gson = new Gson();
                Reader reqBody = new InputStreamReader(exchange.getRequestBody());
                RegisterRequest request = (RegisterRequest)gson.fromJson(reqBody, RegisterRequest.class);

                // Execute register logic via RegisterService object
                RegisterService service = new RegisterService(locData, fmlNames, mlNames, srNames);
                RegisterResult result = service.register(request);

                // Send HTTP Response
                if (result.isSuccess()) {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                } else {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                }

                // Serialize RegisterResult to JSON String
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
