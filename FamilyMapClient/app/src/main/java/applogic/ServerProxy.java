package applogic;

import requests.LoginRequest;
import requests.RegisterRequest;
import results.LoginResult;
import results.RegisterResult;

public class ServerProxy {
    private String serverHost;
    private String serverPort;

    public ServerProxy(String serverHost, String serverPort) {
        this.serverHost = serverHost;
        this.serverPort = serverPort;
    }

    public LoginResult login(LoginRequest request) { return null; }
    public RegisterResult register(RegisterRequest request) { return null; }
}
