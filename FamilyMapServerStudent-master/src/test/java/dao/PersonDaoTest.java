package dao;

import model.Person;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class PersonDaoTest {
    private Database db;
    private Person bestPerson;
    private PersonDao pDao;

    @BeforeEach
    public void setUp() throws DataAccessException {
        // Initialize a Database and an arbitrary Person object
        db = new Database();
        bestPerson = new Person("123b", "Ben123", "Ben", "Button",
                "m", "123f", "123m", "123s");
        // Initialize a connection to the database and clear any existing data from the database
        Connection conn = db.getConnection();
        db.clearTables();
        // Pass the Database Connection to the PersonDao object
        pDao = new PersonDao(conn);
    }

    @AfterEach
    public void tearDown() throws DataAccessException {
        // Close the connection and do not commit changes from tests to the database
        db.closeConnection(false);
    }

    @Test
    public void insertPass() throws DataAccessException {
        // Call the insert method on the pDao (defined in setUp()) and pass it bestPerson (defined in setUp())
        pDao.insert(bestPerson);
        // Call the find method on pDao, looking for bestPerson
        Person compareTest = pDao.find(bestPerson.getId());
        // Determine that a Person object was returned
        assertNotNull(compareTest);
        // Determine that the returning Person object equals bestPerson
        assertEquals(bestPerson, compareTest);
    }

    @Test
    public void insertFail() throws DataAccessException {
        // Insert bestPerson (defined in setUp()) to the database
        pDao.insert(bestPerson);
        // The database should not allow duplicate person IDs so,
        // attempting to insert bestPerson again should result in a DataAccessException
        assertThrows(DataAccessException.class, ()-> pDao.insert(bestPerson));
    }

    @Test
    public void findPass() throws DataAccessException {
        // Call the find method on pDao, looking for bestPerson
        Person compareTest = pDao.find(bestPerson.getId());
        // Determine that a null reference was returned
        assertNull(compareTest);
    }

    @Test
    public void findFail() throws DataAccessException {
        // Close the database connection
        db.closeConnection(false);
        // The find method should not work because the connection is now closed
        assertThrows(DataAccessException.class, ()-> pDao.find(bestPerson.getId()));
        // Reopen the connection so the tearDown() method does not fail
        db.openConnection();
    }

    @Test
    public void clearPass() throws DataAccessException {
        // Call the insert method on the pDao (defined in setUp()) and pass it bestPerson (defined in setUp())
        pDao.insert(bestPerson);
        // Call the clear method on pDao
        pDao.clear();
        // Call the find method on pDao, looking for bestPerson
        Person compareTest = pDao.find(bestPerson.getId());
        // Determine that a null reference was returned
        assertNull(compareTest);
    }
}
