package service;

import dao.*;
import model.AuthToken;
import model.Event;
import model.Person;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import results.FamilyEventsResult;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class FamilyEventsServiceTest {
    private Database db;
    private FamilyEventsService service;
    private FamilyEventsResult result;

    @BeforeEach
    public void setUp() throws DataAccessException {
        // Initialize Service Object
        service = new FamilyEventsService();
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
    public void familyEventsInvalidAuthToken() {
        // Call the Service with any AuthToken
        result = service.familyEvents("xaybzc123");
        // This should result in an Invalid AuthToken error b/c an AuthToken has not been generated for the User
        assertFalse(result.isSuccess());
        assertEquals("Error: Invalid auth token.", result.getMessage());
    }

    @Test
    public void familySuccess() throws DataAccessException {
        db.openConnection();
        // Add AuthToken to Database
        AuthToken authToken = new AuthToken("xaybzc123", "Ben123");
        new AuthTokenDao(db.getConnection()).insert(authToken);

        // Add person to Database
        Person person = new Person("123b", "Ben123", "Ben", "Button",
                "m", "123f", "123m", "123s");
        PersonDao pDao = new PersonDao(db.getConnection());
        pDao.insert(person);

        // Create Event objects for above Person and add them to the Database
        Event event1 = new Event("123e1", "Ben123", "123b", 10.0F, 10.0F,
                                "USA", "Provo", "birth", 2000);
        Event event2 = new Event("123e2", "Ben123", "123b", 20.0F, 20.0F,
                "USA", "Provo", "death", 2070);
        EventDao eDao = new EventDao(db.getConnection());
        eDao.insert(event1);
        eDao.insert(event2);

        // Create Person objects for person's family members and add them to the database
        Person spouse = new Person("123s", "Ben123", "Spouse", "Button",
                "f", "321f", "321m", "123b");
        Person father = new Person("123f", "Ben123", "Father", "Button",
                "m", "000f", "000m", "123m");
        Person mother = new Person("123m", "Ben123", "Mother", "Button",
                "f", "111f", "111m", "123f");
        pDao.insert(spouse);
        pDao.insert(father);
        pDao.insert(mother);

        // Create Event Objects for family members and insert them into the Database
        Event eventf1 = new Event("123ef1", "Ben123", "123f", 70.0F, 70.0F,
                "USA", "Provo", "birth", 1975);
        Event eventf2 = new Event("123ef2", "Ben123", "123f", 80.0F, 80.0F,
                "USA", "Provo", "death", 2045);
        Event eventm1 = new Event("123em1", "Ben123", "123m", 50.0F, 50.0F,
                "USA", "Provo", "birth", 1975);
        Event eventm2 = new Event("123em2", "Ben123", "123m", 60.0F, 60.0F,
                "USA", "Provo", "death", 2045);
        Event events1 = new Event("123es1", "Ben123", "123s", 30.0F, 30.0F,
                "USA", "Provo", "birth", 1975);
        Event events2 = new Event("123es2", "Ben123", "123s", 40.0F, 40.0F,
                "USA", "Provo", "death", 2045);
        eDao.insert(eventf1);
        eDao.insert(eventf2);
        eDao.insert(eventm1);
        eDao.insert(eventm2);
        eDao.insert(events1);
        eDao.insert(events2);

        // Close connection
        db.closeConnection(true);

        // Call the Service with the correct AuthToken
        result = service.familyEvents("xaybzc123");

        // This should result in a successful response with every family member's events in the list
        assertTrue(result.isSuccess());
        assertEquals(8, result.getData().size());
        assertTrue(result.getData().contains(event1));
        assertTrue(result.getData().contains(event2));
        assertTrue(result.getData().contains(eventf1));
        assertTrue(result.getData().contains(eventf2));
        assertTrue(result.getData().contains(eventm1));
        assertTrue(result.getData().contains(eventm2));
        assertTrue(result.getData().contains(events1));
        assertTrue(result.getData().contains(events2));
    }

    @Test
    public void familyEventsEmptySuccess() throws DataAccessException {
        db.openConnection();
        // Add AuthToken to Database
        AuthToken authToken = new AuthToken("xaybzc123", "Ben123");
        new AuthTokenDao(db.getConnection()).insert(authToken);

        // Add person to Database
        Person person = new Person("123b", "Ben123", "Ben", "Button",
                "m", "123f", "123m", "123s");
        PersonDao pDao = new PersonDao(db.getConnection());
        pDao.insert(person);

        // Close connection
        db.closeConnection(true);

        // Call the Service with the correct AuthToken
        result = service.familyEvents("xaybzc123");

        // This should result in a successful response with an empty list
        assertTrue(result.isSuccess());
        assertTrue(result.getData().isEmpty());
    }
}
