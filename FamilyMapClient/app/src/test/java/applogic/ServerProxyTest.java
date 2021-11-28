package applogic;


import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import requests.LoginRequest;
import requests.RegisterRequest;
import results.LoginResult;
import results.RegisterResult;

public class ServerProxyTest {
    private final ServerProxy serverProxy = new ServerProxy("localhost", "8080");
    private final LoginRequest loginRequest = new LoginRequest("bob", "xyz123");
    private final RegisterRequest registerRequest = new RegisterRequest("bob", "xyz123",
                                                                  "bob@cs.byu.edu", "Bob",
                                                                  "Sanders", "m");

    @BeforeEach
    public void setUp() {
        serverProxy.clearDatabase();
    }

    @AfterEach
    public void cleanUp() {
        serverProxy.clearDatabase();
    }

    @Test
    public void loginInvalidUser() {
        // Attempt to login the user defined in loginRequest
        LoginResult loginResult = serverProxy.login(loginRequest);

        // Request should have failed due to non-registered user
        assertFalse(loginResult.isSuccess());
        assertEquals("Error: Username is not registered.", loginResult.getMessage());
    }

    @Test
    public void loginInvalidPassword() {
        // Register the user defined in registerRequest
        serverProxy.register(registerRequest);

        // Attempt to login in a user with the same username but different password
        LoginResult loginResult = serverProxy.login(new LoginRequest("bob", "x"));

        // Request should have failed because of the incorrect password
        assertFalse(loginResult.isSuccess());
        assertEquals("Error: Invalid password.", loginResult.getMessage());
    }

    @Test
    public void loginSuccess() {
        // Register the user defined in registerRequest
        serverProxy.register(registerRequest);

        // Attempt to login the user defined in loginRequest
        LoginResult loginResult = serverProxy.login(loginRequest);

        // Request should have succeeded
        assertTrue(loginResult.isSuccess());
        assertNotNull(loginResult.getAuthtoken());
        assertNotNull(loginResult.getUsername());
        assertNotNull(loginResult.getPersonID());
    }

    @Test
    public void registerUsernameTaken() {
        // Register the user defined in registerRequest
        serverProxy.register(registerRequest);

        // Attempt to register the same user
        RegisterResult registerResult = serverProxy.register(registerRequest);

        // Request should have failed because the username is already taken
        assertFalse(registerResult.isSuccess());
        assertEquals("Error: Username already taken by another user.", registerResult.getMessage());
    }

    @Test
    public void registerSuccess() {
        // Register the user defined in registerRequest
        RegisterResult registerResult = serverProxy.register(registerRequest);

        // Request should have been successful
        assertTrue(registerResult.isSuccess());
        assertNotNull(registerResult.getAuthtoken());
        assertNotNull(registerResult.getUsername());
        assertNotNull(registerResult.getPersonID());
    }
}
