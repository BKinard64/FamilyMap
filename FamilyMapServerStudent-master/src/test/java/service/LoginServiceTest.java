package service;

import dao.AuthTokenDao;
import dao.DataAccessException;
import dao.Database;
import dao.UserDao;
import model.AuthToken;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.requests.LoginRequest;
import service.results.LoginResult;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;

public class LoginServiceTest {
    private Database db;
    private User bestUser;
    private LoginRequest request;
    private LoginService bestService;
    private LoginResult result;

    @BeforeEach
    public void setUp() throws DataAccessException {
        // Initialize LoginRequest and LoginService Objects
        request = new LoginRequest("Ben123", "1111");
        bestService = new LoginService();
        // Initialize a Database and an arbitrary User object
        db = new Database();
        bestUser = new User("Ben123", "1111", "ben@cs.byu.edu", "Ben",
                "Button", "m", "123b");
        // Initialize a connection to the database and clear any existing data from the database
        Connection conn = db.getConnection();
        db.clearTables();
        // Pass the Database Connection to the UserDao object and insert bestUser into database
        new UserDao(conn).insert(bestUser);
        // Close the connection and commit the change
        db.closeConnection(true);
    }

    @AfterEach
    public void tearDown() throws DataAccessException {
        // Open database connection
        db.openConnection();
        // Clear all tables from data that was changed during tests
        db.clearTables();
        // Close the connection and commit clearing of tables
        db.closeConnection(true);
    }

    @Test
    public void loginValidUser() {
        // Call login on bestService
        result = bestService.login(request);
        // The response should return an AuthToken
        assertNotNull(result.getAuthtoken());
        // The response should return the username sent in the request
        assertEquals(request.getUsername(), result.getUsername());
        // The response should return the personID associated with bestUser
        assertEquals(bestUser.getPersonID(), result.getPersonID());
        // The success indicator should be true
        assertTrue(result.isSuccess());
    }

    @Test
    public void checkAuthTokenTable() throws DataAccessException {
        // Call login on bestService
        result = bestService.login(request);
        // Get the AuthToken from the result object
        String tokenString = result.getAuthtoken();
        // Open database connection
        db.openConnection();
        // Search for AuthToken in Database
        AuthToken authToken = new AuthTokenDao(db.getConnection()).find(tokenString);
        // Close database connection
        db.closeConnection(false);
        // AuthToken should have been returned
        assertNotNull(authToken);
        // AuthToken should match AuthToken returned by result
        assertEquals(result.getAuthtoken(), authToken.getToken());
        // Username associated with token should match request/result username
        assertEquals(result.getUsername(), authToken.getUsername());
    }

    @Test
    public void loginInvalidUser() {
        // Create a request with an invalid username
        LoginRequest badRequest = new LoginRequest("ABC", "123");
        // Call login on bestService with badRequest
        result = bestService.login(badRequest);
        // Result should not be null
        assertNotNull(result);
        // Message should be because of invalid username
        assertEquals("Error: Username is not registered.", result.getMessage());
        // The success indicator should be false
        assertFalse(result.isSuccess());
    }

    @Test
    public void loginInvalidPassword() {
        // Create a request with an invalid password
        LoginRequest badRequest = new LoginRequest("Ben123", "123");
        // Call login on bestService with badRequest
        result = bestService.login(badRequest);
        // Result should not be null
        assertNotNull(result);
        // Message should be because of invalid password
        assertEquals("Error: Invalid password.", result.getMessage());
        // The success indicator should be false
        assertFalse(result.isSuccess());
    }
}
