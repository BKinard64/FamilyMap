package service;

import dao.*;
import model.AuthToken;
import model.Event;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.requests.EventRequest;
import service.requests.PersonRequest;
import service.results.EventResult;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class EventServiceTest {
    private Database db;
    private EventRequest request;
    private EventService service;
    private EventResult result;

    @BeforeEach
    public void setUp() throws DataAccessException {
        // Initialize Request and Service Objects
        request = new EventRequest("123e", "xaybzc123");
        service = new EventService();
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
        result = service.event(request);
        // This should result in an Invalid AuthToken error b/c an AuthToken has not been generated for the User
        assertFalse(result.isSuccess());
        assertEquals("Error: Invalid auth token.", result.getMessage());
    }

    @Test
    public void eventIDDoesNotBelong() throws DataAccessException {
        db.openConnection();
        // Add AuthToken to Database
        AuthToken authToken = new AuthToken("xaybzc123", "Ben123");
        new AuthTokenDao(db.getConnection()).insert(authToken);

        // Create Event Object that does not belong to User/Person created in setUp()
        Event badEvent = new Event("321e", "Braden321", "321b", 37.0, 35.0,
                                    "USA", "Provo", "Birth", 2020);
        new EventDao(db.getConnection()).insert(badEvent);
        db.closeConnection(true);

        // Create Request Object with incorrect badEventID
        EventRequest badRequest = new EventRequest("321e", "xaybzc123");
        // Call the Service with the request object defined above
        result = service.event(badRequest);
        // This should result in an error b/c the eventID does not belong to the User identified by the AuthToken
        assertFalse(result.isSuccess());
        assertEquals("Error: Requested event does not belong to this user.", result.getMessage());
    }

    @Test
    public void eventNotInDatabase() throws DataAccessException {
        db.openConnection();
        // Add AuthToken to Database
        AuthToken authToken = new AuthToken("xaybzc123", "Ben123");
        new AuthTokenDao(db.getConnection()).insert(authToken);
        db.closeConnection(true);
        // Call the Service with the request object as defined in setUp
        result = service.event(request);
        // This should result in an error b/c an Event Object for the eventID provided is not in Database
        assertFalse(result.isSuccess());
        assertEquals("Error: Invalid eventID parameter.", result.getMessage());
    }

    @Test
    public void eventSuccess() throws DataAccessException {
        db.openConnection();

        // Add AuthToken to Database
        AuthToken authToken = new AuthToken("xaybzc123", "Ben123");
        new AuthTokenDao(db.getConnection()).insert(authToken);

        // Add Event to Database
        Event event = new Event("123e", "Ben123", "123b", 37.0, 35.0,
                "USA", "Provo", "Birth", 2020);
        new EventDao(db.getConnection()).insert(event);
        db.closeConnection(true);

        // Call the Service with the request object as defined in setUp
        result = service.event(request);
        // This should result in a successful response
        assertTrue(result.isSuccess());
        assertEquals("Ben123", result.getAssociatedUsername());
        assertEquals("123e", result.getEventID());
        assertEquals("123b", result.getPersonID());
        assertEquals(37.0, result.getLatitude());
        assertEquals(35.0, result.getLongitude());
        assertEquals("USA", result.getCountry());
        assertEquals("Provo", result.getCity());
        assertEquals("Birth", result.getEventType());
        assertEquals(2020, result.getYear());
    }
}
