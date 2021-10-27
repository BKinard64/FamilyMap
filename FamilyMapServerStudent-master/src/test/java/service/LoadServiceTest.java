package service;

import dao.*;
import model.AuthToken;
import model.Event;
import model.Person;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.requests.LoadRequest;
import service.results.LoadResult;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class LoadServiceTest {
    private Database db;
    private LoadService service;
    private User bestUser;
    private Person bestPerson;
    private Event bestEvent;
    private UserDao uDao;
    private PersonDao pDao;
    private EventDao eDao;
    private List<User> users;
    private List<Person> persons;
    private List<Event> events;

    @BeforeEach
    public void setUp() throws DataAccessException {
        // Initialize LoadService Object
        service = new LoadService();
        // Initialize List Objects
        users = new ArrayList<>();
        persons = new ArrayList<>();
        events = new ArrayList<>();
        // Initialize a Database and arbitrary User/Person/Event/AuthToken objects
        db = new Database();
        bestUser = new User("Ben123", "1111", "ben@cs.byu.edu", "Ben",
                "Button", "m", "123b");
        bestPerson = new Person("123b", "Ben123", "Ben", "Button", "m",
                "123f", "123,", "123s");
        bestEvent = new Event("456e", "Ben123", "123b", 39.0, 37.0, "USA",
                "Provo", "Birth", 2020);
        AuthToken bestAuthToken = new AuthToken("xaybzc123", "Ben123");
        // Initialize a connection to the database and clear any existing data from the database
        Connection conn = db.getConnection();
        db.clearTables();
        // Pass the Database Connection to the UserDao/PersonDao/EventDao/AuthTokenDao objects
        uDao = new UserDao(conn);
        pDao = new PersonDao(conn);
        eDao = new EventDao(conn);
        AuthTokenDao atDao = new AuthTokenDao(conn);
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
    public void loadSuccess() throws DataAccessException {
        // Create new User list to be loaded
        User newUser1 = new User("John123", "abc", "john@cs.byu.edu", "John",
                                "Johnson", "m", "123j");
        User newUser2 = new User("Peter123", "xyz", "peter@cs.byu.edu", "Peter",
                "Petersen", "m", "123p");
        users.add(newUser1);
        users.add(newUser2);

        // Create new Person list to be loaded
        Person newPerson1 = new Person("123j", "John123", "John", "Johnson",
                                        "m", "Father123", "Mother123", "Spouse123");
        Person newPerson2 = new Person("123p", "Peter123", "Peter", "Petersen",
                "m", "Father321", "Mother321", "Spouse321");
        persons.add(newPerson1);
        persons.add(newPerson2);

        // Create new Event list to be loaded
        Event newEvent1 = new Event("123e", "John123", "123j", 20.0, 20.0,
                                    "USA", "Provo", "birth", 0);
        Event newEvent2 = new Event("321e", "Peter123", "123p", 30.0, 30.0,
                "USA", "Provo", "birth", 0);
        events.add(newEvent1);
        events.add(newEvent2);

        // Create LoadRequest Object
        LoadRequest request = new LoadRequest(users, persons, events);

        // Call load on LoadService object
        LoadResult result = service.load(request);

        // This should have resulted in a successful response
        assertTrue(result.isSuccess());
        assertEquals("Successfully added 2 users, 2 persons, and 2 events to the database.",
                    result.getMessage());

        // The data inserted in setUp() should no longer be there and the
        // new data should be in the database now
        db.openConnection();

        uDao = new UserDao(db.getConnection());
        pDao = new PersonDao(db.getConnection());
        eDao = new EventDao(db.getConnection());

        assertNull(uDao.find(bestUser.getUsername()));
        assertNull(pDao.find(bestPerson.getId()));
        assertNull(eDao.find(bestEvent.getId()));

        assertNotNull(uDao.find(newEvent1.getUsername()));
        assertNotNull(uDao.find(newEvent2.getUsername()));
        assertNotNull(pDao.find(newPerson1.getId()));
        assertNotNull(pDao.find(newPerson2.getId()));
        assertNotNull(eDao.find(newEvent1.getId()));
        assertNotNull(eDao.find(newEvent2.getId()));

        db.closeConnection(true);
    }
}
