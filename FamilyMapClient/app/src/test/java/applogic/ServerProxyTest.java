package applogic;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import requests.LoginRequest;
import results.LoginResult;

public class ServerProxyTest {
    private ServerProxy serverProxy = new ServerProxy("localhost", "8080");
    private LoginRequest loginRequest = new LoginRequest("bob", "xyz123");

    @Test
    public void loginInvalidUser() {
        // Attempt to login the user defined in loginRequest
        LoginResult loginResult = serverProxy.login(loginRequest);

        // Request should have failed due to non-registered user
        assertFalse(loginResult.isSuccess());
        assertEquals("Error: Username is not registered", loginResult.getMessage());
    }
}
