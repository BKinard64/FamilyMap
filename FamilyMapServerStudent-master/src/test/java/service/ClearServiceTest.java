package service;

import dao.*;
import model.AuthToken;
import model.Event;
import model.Person;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import results.ClearResult;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;

public class ClearServiceTest {
    private Database db;
    private User bestUser;
    private Person bestPerson;
    private Event bestEvent;
    private AuthToken bestAuthToken;
    private UserDao uDao;
    private PersonDao pDao;
    private EventDao eDao;
    private AuthTokenDao atDao;
    private ClearService bestService;

    @BeforeEach
    public void setUp() throws DataAccessException {
        // Initialize ClearService Object
        bestService = new ClearService();
        // Initialize a Database and arbitrary User/Person/Event/AuthToken objects
        db = new Database();
        bestUser = new User("Ben123", "1111", "ben@cs.byu.edu", "Ben",
                "Button", "m", "123b");
        bestPerson = new Person("123b", "Ben123", "Ben", "Button", "m",
                "123f", "123,", "123s");
        bestEvent = new Event("123e", "Ben123", "123b", 39.0F, 37.0F, "USA",
                "Provo", "Birth", 2020);
        bestAuthToken = new AuthToken("xaybzc123", "Ben123");
        // Initialize a connection to the database and clear any existing data from the database
        Connection conn = db.getConnection();
        db.clearTables();
        // Pass the Database Connection to the UserDao/PersonDao/EventDao/AuthTokenDao objects
        uDao = new UserDao(conn);
        pDao = new PersonDao(conn);
        eDao = new EventDao(conn);
        atDao = new AuthTokenDao(conn);
        // Insert the model objects into the database
        uDao.insert(bestUser);
        pDao.insert(bestPerson);
        eDao.insert(bestEvent);
        atDao.insert(bestAuthToken);
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
    public void clearSuccess() throws DataAccessException {
        // Call clear on bestService
        ClearResult result = bestService.clear();
        // Clear should have been successful
        assertEquals("Clear succeeded.", result.getMessage());
        assertTrue(result.isSuccess());
        // Initialize the DAO objects with new connections
        db.openConnection();
        uDao = new UserDao(db.getConnection());
        pDao = new PersonDao(db.getConnection());
        eDao = new EventDao(db.getConnection());
        atDao = new AuthTokenDao(db.getConnection());
        // The database should be empty now
        assertNull(uDao.find(bestUser.getUsername()));
        assertNull(pDao.find(bestPerson.getId()));
        assertNull(eDao.find(bestEvent.getId()));
        assertNull(atDao.find(bestAuthToken.getToken()));
        // Close connection
        db.closeConnection(false);
    }
}
