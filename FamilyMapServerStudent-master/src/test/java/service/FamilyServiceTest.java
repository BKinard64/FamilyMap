package service;

import dao.*;
import model.AuthToken;
import model.Person;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.results.FamilyResult;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class FamilyServiceTest {
    private Database db;
    private FamilyService service;
    private FamilyResult result;

    @BeforeEach
    public void setUp() throws DataAccessException {
        // Initialize Service Object
        service = new FamilyService();
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
    public void familyInvalidAuthToken() {
        // Call the Service with any AuthToken
        result = service.family("xaybzc123");
        // This should result in an Invalid AuthToken error b/c an AuthToken has not been generated for the User
        assertFalse(result.isSuccess());
        assertEquals("Error: Invalid auth token.", result.getMessage());
    }

    @Test
    public void personNotInDatabase() throws DataAccessException {
        db.openConnection();
        // Add AuthToken to Database
        AuthToken authToken = new AuthToken("xaybzc123", "Ben123");
        new AuthTokenDao(db.getConnection()).insert(authToken);
        db.closeConnection(true);
        // Call the Service with the correct AuthToken
        result = service.family("xaybzc123");
        // This should result in an error b/c a Person Object for the personID associated w/ the User is not in Database
        assertFalse(result.isSuccess());
        assertEquals("Error: Internal server error.", result.getMessage());
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

        // Create Person objects for person's family members and add them to the database
        Person spouse = new Person("123s", "Spouse123", "Spouse", "Button",
                "f", "321f", "321m", "123b");
        Person father = new Person("123f", "Father123", "Father", "Button",
                "m", "000f", "000m", "123m");
        Person mother = new Person("123m", "Mother123", "Mother", "Button",
                "f", "111f", "111m", "123f");
        pDao.insert(spouse);
        pDao.insert(father);
        pDao.insert(mother);

        // Close connection
        db.closeConnection(true);

        // Call the Service with the correct AuthToken
        result = service.family("xaybzc123");

        // This should result in a successful response with each of the family remembers returned in a list
        assertTrue(result.isSuccess());
        assertEquals(3, result.getData().size());
        assertTrue(result.getData().contains(spouse));
        assertTrue(result.getData().contains(father));
        assertTrue(result.getData().contains(mother));
    }

    @Test
    public void familyEmptySuccess() throws DataAccessException {
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
        result = service.family("xaybzc123");

        // This should result in a successful response with an empty list
        assertTrue(result.isSuccess());
        assertTrue(result.getData().isEmpty());
    }
}
