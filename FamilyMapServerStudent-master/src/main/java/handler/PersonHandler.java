package handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import dao.*;
import model.AuthToken;
import model.Person;
import model.User;
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
                    // Create Database connection
                    Database db = new Database();
                    db.openConnection();
                    // Create AuthTokenDao to validate AuthToken
                    AuthTokenDao atDao = new AuthTokenDao(db.getConnection());
                    AuthToken authToken = atDao.find(tokenString);

                    if (authToken != null) {

                        // Create UserDao to validate personID or to get associated Person object
                        UserDao uDao = new UserDao(db.getConnection());
                        User user = uDao.find(authToken.getUsername());

                        // Get the URL path from the Request
                        String urlPath = exchange.getRequestURI().toString();

                        // If the path specifies a specific PersonID, call the PersonService
                        if (urlPath.length() > 8) {

                            // Close database connection
                            db.closeConnection(false);

                            // Extract the personID from the URL
                            String personID = urlPath.substring(8);
                            // Confirm specified PersonID belongs to current user
                            if (user.getPersonID().equals(personID)) {

                                // Create PersonRequest object to pass to PersonService
                                PersonRequest pRequest = new PersonRequest(personID);

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
                                // Specified personID does not belong to current user
                                exchange.sendResponseHeaders(HttpURLConnection.HTTP_UNAUTHORIZED, 0);
                                // Close the output stream for the response body
                                exchange.getResponseBody().close();
                            }
                        } else {
                            // The path does not specify a PersonID, so call FamilyService instead

                            // Find Person associated with Username
                            PersonDao pDao = new PersonDao(db.getConnection());
                            Person person = pDao.find(user.getPersonID());

                            // Close database connection
                            db.closeConnection(false);

                            // Execute Request Logic in FamilyService
                            FamilyService fService = new FamilyService();
                            FamilyResult fResult = fService.family(person);

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
                        // The AuthToken is not valid
                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_UNAUTHORIZED, 0);
                        // Close the output stream for the response body
                        exchange.getResponseBody().close();
                        // Close database connection
                        db.closeConnection(false);
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
        } catch (IOException | DataAccessException e) {
            // Report an internal server error
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_SERVER_ERROR, 0);
            // Close the output stream for the response body
            exchange.getResponseBody().close();
            // Log the stack trace
            e.printStackTrace();
        }
    }
}
