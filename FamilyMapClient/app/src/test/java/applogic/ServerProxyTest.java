package applogic;


import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import requests.LoginRequest;
import requests.RegisterRequest;
import results.FamilyEventsResult;
import results.FamilyResult;
import results.LoadResult;
import results.LoginResult;
import results.RegisterResult;

public class ServerProxyTest {
    private final ServerProxy serverProxy = new ServerProxy("localhost", "8080");
    private final LoginRequest loginRequest = new LoginRequest("sheila", "parker");
    private final RegisterRequest registerRequest = new RegisterRequest("bob", "xyz123",
                                                                  "bob@cs.byu.edu", "Bob",
                                                                  "Sanders", "m");

    @BeforeEach
    public void setUp() {
        serverProxy.load();
        DataCache.getInstance().clear();
    }

    @AfterEach
    public void cleanUp() {
        serverProxy.load();
        DataCache.getInstance().clear();
    }

    @Test
    public void loginInvalidUser() {
        // Attempt to login an un-registered user
        LoginResult loginResult = serverProxy.login(new LoginRequest("bob", "sanders"));

        // Request should have failed due to non-registered user
        assertFalse(loginResult.isSuccess());
        assertEquals("Error: Username is not registered.", loginResult.getMessage());
    }

    @Test
    public void loginInvalidPassword() {
        // Attempt to login in a user with the same username but different password
        LoginResult loginResult = serverProxy.login(new LoginRequest("sheila", "x"));

        // Request should have failed because of the incorrect password
        assertFalse(loginResult.isSuccess());
        assertEquals("Error: Invalid password.", loginResult.getMessage());
    }

    @Test
    public void loginSuccess() {
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
        // Attempt to get the family without logging in a user
        FamilyResult familyResult = serverProxy.getFamily();

        // Request should have failed due to no authtoken being provided
        assertFalse(familyResult.isSuccess());
        assertEquals("Error: Invalid auth token.", familyResult.getMessage());
    }

    @Test
    public void getFamilySuccess() {
        // Login with Sheila Parker
        LoginResult loginResult = serverProxy.login(loginRequest);
        DataCache.getInstance().setAuthToken(loginResult.getAuthtoken());

        // Attempt to get the family of the registered user
        FamilyResult familyResult = serverProxy.getFamily();

        // Request should have been successful
        assertTrue(familyResult.isSuccess());
        assertNotNull(familyResult.getData());
    }

    @Test
    public void getFamilyEventsInvalidAuthToken() {
        // Attempt to get the family events without logging in a user
        FamilyEventsResult familyEventsResult = serverProxy.getFamilyEvents();

        // Attempt should have failed due to no auth token being provided
        assertFalse(familyEventsResult.isSuccess());
        assertEquals("Error: Invalid auth token.", familyEventsResult.getMessage());
    }

    @Test
    public void getFamilyEventsSuccess() {
        // Login with Sheila Parker
        LoginResult loginResult = serverProxy.login(loginRequest);
        DataCache.getInstance().setAuthToken(loginResult.getAuthtoken());

        // Attempt to get the family events of the registered user
        FamilyEventsResult familyEventsResult = serverProxy.getFamilyEvents();

        // Attempt should have been successful
        assertTrue(familyEventsResult.isSuccess());
        assertNotNull(familyEventsResult.getData());
    }

    @Test
    public void loadSuccess() {
        LoadResult result = serverProxy.load();

        assertTrue(result.isSuccess());
        assertEquals("Successfully added 2 users, 11 persons, and 19 events to the database.",
                    result.getMessage());
    }
}
