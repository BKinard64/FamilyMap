package dao;

import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.xml.crypto.Data;
import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class UserDaoTest {
    private Database db;
    private User bestUser;
    private UserDao uDao;

    @BeforeEach
    public void setUp() throws DataAccessException {
        // Initialize a Database and an arbitrary User object
        db = new Database();
        bestUser = new User("Ben123", "1111", "ben@cs.byu.edu", "Ben",
                "Button", "m", "123b");
        // Initialize a connection to the database and clear any existing data from the database
        Connection conn = db.getConnection();
        db.clearTables();
        // Pass the Database Connection to the UserDao object
        uDao = new UserDao(conn);
    }

    @AfterEach
    public void tearDown() throws DataAccessException {
        // Close the connection and do not commit changes from tests to the database
        db.closeConnection(false);
    }

    @Test
    public void insertPass() throws DataAccessException {
        // Call the insert method on the uDao (defined in setUp()) and pass it bestUser (defined in setUp())
        uDao.insert(bestUser);
        // Call the find method on uDao, looking for bestUser
        User compareTest = uDao.find(bestUser.getUsername());
        // Determine that a User object was returned
        assertNotNull(compareTest);
        // Determine that the returning User object equals bestUser
        assertEquals(bestUser, compareTest);
    }

    @Test
    public void insertFail() throws DataAccessException {
        // Insert bestUser (defined in setUp()) to the database
        uDao.insert(bestUser);
        // The database should not allow duplicate usernames so,
        // attempting to insert bestUser again should result in a DataAccessException
        assertThrows(DataAccessException.class, ()-> uDao.insert(bestUser));
    }
}
