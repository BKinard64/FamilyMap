package applogic;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;

import requests.LoginRequest;
import requests.RegisterRequest;
import results.LoginResult;
import results.RegisterResult;

public class ServerProxy {
    private final String serverHost;
    private final String serverPort;

    public ServerProxy(String serverHost, String serverPort) {
        this.serverHost = serverHost;
        this.serverPort = serverPort;
    }

    public LoginResult login(LoginRequest request) {
        try {

            // Begin creating request to send to Server
            URL url = new URL("http://" + serverHost + ":" + serverPort + "/user/login");
            HttpURLConnection http = (HttpURLConnection)url.openConnection();

            // Set method to POST and establish there will be a Request Body
            http.setRequestMethod("POST");
            http.setDoOutput(true);

            // Connect to the Server
            http.connect();

            // Write the Request Body from the LoginRequest
            Gson gson = new Gson();
            Writer reqBody = new OutputStreamWriter(http.getOutputStream());
            gson.toJson(request, reqBody);
            reqBody.close();

            // Deserialize the Response Body to a LoginResult
            Reader resBody;
            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                resBody = new InputStreamReader(http.getInputStream());
            } else {
                resBody = new InputStreamReader(http.getErrorStream());
            }

            return (LoginResult)gson.fromJson(resBody, LoginResult.class);

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    public RegisterResult register(RegisterRequest request) { return null; }
}
