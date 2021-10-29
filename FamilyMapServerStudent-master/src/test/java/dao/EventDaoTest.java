package dao;

import model.Event;
import model.Person;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class EventDaoTest {
    private Database db;
    private Event bestEvent;
    private EventDao eDao;

    @BeforeEach
    public void setUp() throws DataAccessException {
        // Initialize a Database and an arbitrary Event object
        db = new Database();
        bestEvent = new Event("123e", "Ben123", "123b", 39.0F, 43.0F,
                "USA", "Provo", "Birth", 2016);
        // Initialize a connection to the database and clear any existing data from the database
        Connection conn = db.getConnection();
        db.clearTables();
        // Pass the Database Connection to the EventDao object
        eDao = new EventDao(conn);
    }

    @AfterEach
    public void tearDown() throws DataAccessException {
        // Close the connection and do not commit changes from tests to the database
        db.closeConnection(false);
    }

    @Test
    public void insertPass() throws DataAccessException {
        // Call the insert method on the eDao (defined in setUp()) and pass it bestEvent (defined in setUp())
        eDao.insert(bestEvent);
        // Call the find method on eDao, looking for bestEvent
        Event compareTest = eDao.find(bestEvent.getId());
        // Determine that an Event object was returned
        assertNotNull(compareTest);
        // Determine that the returning Event object equals bestEvent
        assertEquals(bestEvent, compareTest);
    }

    @Test
    public void insertFail() throws DataAccessException {
        // Insert bestEvent (defined in setUp()) to the database
        eDao.insert(bestEvent);
        // The database should not allow duplicate event IDs so,
        // attempting to insert bestEvent again should result in a DataAccessException
        assertThrows(DataAccessException.class, ()-> eDao.insert(bestEvent));
    }

    @Test
    public void findPass() throws DataAccessException {
        // Call the find method on pDao, looking for bestPerson
        Event compareTest = eDao.find(bestEvent.getId());
        // Determine that a null reference was returned
        assertNull(compareTest);
    }

    @Test
    public void findFail() throws DataAccessException {
        // Close the database connection
        db.closeConnection(false);
        // The find method should not work because the connection is now closed
        assertThrows(DataAccessException.class, ()-> eDao.find(bestEvent.getId()));
        // Reopen the connection so the tearDown() method does not fail
        db.openConnection();
    }

    @Test
    public void getFamilyEventsEmpty() throws DataAccessException {
        // Initialize Person object
        Person bestPerson = new Person("123b", "Ben123", "Ben", "Button",
                "m", "123f", "123m", "123s");
        // Get the Events of bestPerson's family
        List<Event> famEvents = eDao.getFamilyEvents(bestPerson.getUsername());
        // The List should be empty because no Event objects are in the database
        assertTrue(famEvents.isEmpty());
    }

    @Test
    public void getFamilyEventsPass() throws DataAccessException {
        // Initialize Person object and PersonDao object
        Person bestPerson = new Person("123b", "Ben123", "Ben", "Button",
                "m", "123f", "123m", "123s");
        PersonDao pDao = new PersonDao(db.getConnection());
        // Insert bestPerson (defined in setUp()) to the database
        pDao.insert(bestPerson);
        // Insert bestEvent for bestPerson into database
        eDao.insert(bestEvent);

        // Create Person objects for bestPerson's family members and add them to the database
        Person spouse = new Person("123s", "Ben123", "Spouse", "Button",
                "f", "321f", "321m", "123b");
        Person father = new Person("123f", "Ben123", "Father", "Button",
                "m", "000f", "000m", "123m");
        Person mother = new Person("123m", "Ben123", "Mother", "Button",
                "f", "111f", "111m", "123f");
        pDao.insert(spouse);
        pDao.insert(father);
        pDao.insert(mother);

        // Create an Event object for each member of bestPerson's family and add them to the database
        Event spouseEvent = new Event("123se", "Ben123", "123s", 37.0F, 45.0F,
                "USA", "Salt Lake City", "Birth", 2016);
        Event fatherEvent = new Event("123fe", "Ben123", "123f", 29.0F, 53.0F,
                "USA", "Las Vegas", "Birth", 1990);
        Event motherEvent = new Event("123me", "Ben123", "123m", 30.0F, 40.0F,
                "USA", "Los Angeles", "Birth", 1990);
        eDao.insert(spouseEvent);
        eDao.insert(fatherEvent);
        eDao.insert(motherEvent);

        // Get the Events of bestPerson's family
        List<Event> famEvents = eDao.getFamilyEvents(bestPerson.getUsername());

        // The List should have 4 Event Objects
        assertEquals(4, famEvents.size());

        // The List should contain bestPerson's event and the events of bestPerson's spouse, father, and mother
        assertTrue(famEvents.contains(bestEvent));
        assertTrue(famEvents.contains(spouseEvent));
        assertTrue(famEvents.contains(fatherEvent));
        assertTrue(famEvents.contains(motherEvent));
    }

    @Test
    public void deletePass() throws DataAccessException {
        // Call the insert method on the eDao (defined in setUp()) and pass it bestEvent (defined in setUp())
        eDao.insert(bestEvent);
        // Call the delete method on eDao
        eDao.delete(bestEvent.getId());
        // Call the find method on eDao, looking for bestEvent
        Event compareTest = eDao.find(bestEvent.getId());
        // Determine that a null reference was returned
        assertNull(compareTest);
    }

    @Test
    public void deleteNonExistentPerson() throws DataAccessException {
        // Call the delete method on eDao
        eDao.delete(bestEvent.getId());
        // Even though bestEvent is not in database, delete does not throw an exception
    }

    @Test
    public void deleteFamilyEventsPass() throws DataAccessException {
        // Call the insert method on the eDao (defined in setUp()) and pass it bestEvent (defined in setUp())
        eDao.insert(bestEvent);

        // Create an Event object for each member of bestPerson's family and add them to the database
        Event spouseEvent = new Event("123se", "Ben123", "123s", 37.0F, 45.0F,
                "USA", "Salt Lake City", "Birth", 2016);
        Event fatherEvent = new Event("123fe", "Ben123", "123f", 29.0F, 53.0F,
                "USA", "Las Vegas", "Birth", 1990);
        Event motherEvent = new Event("123me", "Ben123", "123m", 30.0F, 40.0F,
                "USA", "Los Angeles", "Birth", 1990);
        eDao.insert(spouseEvent);
        eDao.insert(fatherEvent);
        eDao.insert(motherEvent);

        // Call the deleteFamilyEvents method on eDao
        eDao.deleteFamilyEvents(bestEvent.getUsername());

        // Confirm bestEvent and family events are no longer in database
        assertNull(eDao.find(bestEvent.getId()));
        assertNull(eDao.find(spouseEvent.getId()));
        assertNull(eDao.find(motherEvent.getId()));
        assertNull(eDao.find(fatherEvent.getId()));
    }

    @Test
    public void deleteFamilyEventsEmptyPass() throws DataAccessException {
        // Call the insert method on the eDao (defined in setUp()) and pass it bestEvent (defined in setUp())
        eDao.insert(bestEvent);
        // Call the deleteFamilyEvents method on eDao
        eDao.deleteFamilyEvents(bestEvent.getUsername());
        // Call the find method on eDao, looking for bestEvent
        Event compareTest = eDao.find(bestEvent.getId());
        // Determine that a null reference was returned
        assertNull(compareTest);
    }

    @Test
    public void clearPass() throws DataAccessException {
        // Call the insert method on the eDao (defined in setUp()) and pass it bestEvent (defined in setUp())
        eDao.insert(bestEvent);
        // Call the clear method on eDao
        eDao.clear();
        // Call the find method on eDao, looking for bestEvent
        Event compareTest = eDao.find(bestEvent.getId());
        // Determine that a null reference was returned
        assertNull(compareTest);
    }
}
