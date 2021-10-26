package service;

import dao.*;
import model.AuthToken;
import model.Person;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.requests.PersonRequest;
import service.results.PersonResult;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;

public class PersonServiceTest {
    private Database db;
    private PersonRequest request;
    private PersonService service;
    private PersonResult result;

    @BeforeEach
    public void setUp() throws DataAccessException {
        // Initialize Request and Service Objects
        request = new PersonRequest("123b", "xaybzc123");
        service = new PersonService();
        // Initialize a Database
        db = new Database();
        // Create an arbitrary User object
        User user = new User("Ben123", "1111", "ben@cs.byu.edu", "Ben",
                        "Button", "m", "123b");
        // Clear all tables from the Database and Insert the User into the Table
        Connection conn = db.getConnection();
        db.clearTables();
        new UserDao(conn).insert(user);
        // Close the connection to the Database
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
    public void personInvalidAuthToken() {
        // Call the Service with the request object as defined in setUp
        result = service.person(request);
        // This should result in an Invalid AuthToken error b/c an AuthToken has not been generated for the User
        assertFalse(result.isSuccess());
        assertEquals("Error: Invalid auth token.", result.getMessage());
    }

    @Test
    public void personIDDoesNotBelong() throws DataAccessException {
        db.openConnection();
        // Add AuthToken to Database
        AuthToken authToken = new AuthToken("xaybzc123", "Ben123");
        new AuthTokenDao(db.getConnection()).insert(authToken);
        db.closeConnection(true);
        // Create Request Object with incorrect personID
        PersonRequest badRequest = new PersonRequest("456b", "xaybzc123");
        // Call the Service with the request object defined above
        result = service.person(badRequest);
        // This should result in an error b/c the personID does not belong to the User identified by the AuthToken
        assertFalse(result.isSuccess());
        assertEquals("Error: Requested person does not belong to this user.", result.getMessage());
    }

    @Test
    public void personNotInDatabase() throws DataAccessException {
        db.openConnection();
        // Add AuthToken to Database
        AuthToken authToken = new AuthToken("xaybzc123", "Ben123");
        new AuthTokenDao(db.getConnection()).insert(authToken);
        db.closeConnection(true);
        // Call the Service with the request object as defined in setUp
        result = service.person(request);
        // This should result in an error b/c a Person Object for the personID provided is not in Database
        assertFalse(result.isSuccess());
        assertEquals("Error: Invalid personID parameter.", result.getMessage());
    }

    @Test
    public void personSuccess() throws DataAccessException {
        db.openConnection();
        // Add AuthToken to Database
        AuthToken authToken = new AuthToken("xaybzc123", "Ben123");
        new AuthTokenDao(db.getConnection()).insert(authToken);
        // Add Person to Database
        Person person = new Person("123b", "Ben123", "Ben", "Button",
                "m", "123f", "123m", "123s");
        new PersonDao(db.getConnection()).insert(person);
        db.closeConnection(true);
        // Call the Service with the request object as defined in setUp
        result = service.person(request);
        // This should result in a successful response
        assertTrue(result.isSuccess());
        assertEquals("Ben123", result.getAssociatedUsername());
        assertEquals("123b", result.getPersonID());
        assertEquals("Ben", result.getFirstName());
        assertEquals("Button", result.getLastName());
        assertEquals("m", result.getGender());
        assertEquals("123f", result.getFatherID());
        assertEquals("123m", result.getMotherID());
        assertEquals("123s", result.getSpouseID());
        assertNull(result.getMessage());
    }

    @Test
    public void personNoFamilySuccess() throws DataAccessException {
        db.openConnection();
        // Add AuthToken to Database
        AuthToken authToken = new AuthToken("xaybzc123", "Ben123");
        new AuthTokenDao(db.getConnection()).insert(authToken);
        // Add Person to Database
        Person person = new Person("123b", "Ben123", "Ben", "Button",
                "m", null, null, null);
        new PersonDao(db.getConnection()).insert(person);
        db.closeConnection(true);
        // Call the Service with the request object as defined in setUp
        result = service.person(request);
        // This should result in a successful response
        assertTrue(result.isSuccess());
        assertEquals("Ben123", result.getAssociatedUsername());
        assertEquals("123b", result.getPersonID());
        assertEquals("Ben", result.getFirstName());
        assertEquals("Button", result.getLastName());
        assertEquals("m", result.getGender());
        assertNull(result.getFatherID());
        assertNull(result.getMotherID());
        assertNull(result.getSpouseID());
        assertNull(result.getMessage());
    }
}
