package applogic;


import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import requests.LoginRequest;
import requests.RegisterRequest;
import results.FamilyEventsResult;
import results.FamilyResult;
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
        DataCache.getInstance().clear();
    }

    @AfterEach
    public void cleanUp() {
        serverProxy.clearDatabase();
        DataCache.getInstance().clear();
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

    @Test
    public void getFamilyInvalidAuthToken() {
        // Attempt to get the family without registering a user
        FamilyResult familyResult = serverProxy.getFamily();

        // Request should have failed due to no authtoken being provided
        assertFalse(familyResult.isSuccess());
        assertEquals("Error: Invalid auth token.", familyResult.getMessage());
    }

    @Test
    public void getFamilySuccess() {
        // Register the user defined in registerRequest
        RegisterResult registerResult = serverProxy.register(registerRequest);
        DataCache.getInstance().setAuthToken(registerResult.getAuthtoken());

        // Attempt to get the family of the registered user
        FamilyResult familyResult = serverProxy.getFamily();

        // Request should have been successful
        assertTrue(familyResult.isSuccess());
        assertNotNull(familyResult.getData());
    }

    @Test
    public void getFamilyEventsInvalidAuthToken() {
        // Attempt to get the family events without registering a user
        FamilyEventsResult familyEventsResult = serverProxy.getFamilyEvents();

        // Attempt should have failed due to no auth token being provided
        assertFalse(familyEventsResult.isSuccess());
        assertEquals("Error: Invalid auth token.", familyEventsResult.getMessage());
    }

    @Test
    public void getFamilyEventsSuccess() {
        // Register the user defined in registerRequest
        RegisterResult registerResult = serverProxy.register(registerRequest);
        DataCache.getInstance().setAuthToken(registerResult.getAuthtoken());

        // Attempt to get the family events of the registered user
        FamilyEventsResult familyEventsResult = serverProxy.getFamilyEvents();

        // Attempt should have been successful
        assertTrue(familyEventsResult.isSuccess());
        assertNotNull(familyEventsResult.getData());
    }
}
