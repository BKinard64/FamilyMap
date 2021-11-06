package dao;

import model.AuthToken;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class AuthTokenDaoTest {
    private Database db;
    private AuthToken bestAuthToken;
    private AuthTokenDao atDao;

    @BeforeEach
    public void setUp() throws DataAccessException {
        // Initialize a Database and an arbitrary User object
        db = new Database();
        bestAuthToken = new AuthToken("Ben123", "a1b2c3");
        // Initialize a connection to the database and clear any existing data from the database
        Connection conn = db.getConnection();
        db.clearTables();
        // Pass the Database Connection to the UserDao object
        atDao = new AuthTokenDao(conn);
    }

    @AfterEach
    public void tearDown() throws DataAccessException {
        // Close the connection and do not commit changes from tests to the database
        db.closeConnection(false);
    }

    @Test
    public void insertPass() throws DataAccessException {
        // Call the insert method on the atDao (defined in setUp()) and pass it bestAuthToken (defined in setUp())
        atDao.insert(bestAuthToken);
        // Call the find method on atDao, looking for bestAuthToken
        AuthToken compareTest = atDao.find(bestAuthToken.getToken());
        // Determine that an AuthToken object was returned
        assertNotNull(compareTest);
        // Determine that the returning AuthToken object equals bestAuthToken
        assertEquals(bestAuthToken, compareTest);
    }

    @Test
    public void insertFail() throws DataAccessException {
        // Insert bestAuthToken (defined in setUp()) to the database
        atDao.insert(bestAuthToken);
        // The database should not allow duplicate auth tokens so,
        // attempting to insert bestAuthToken again should result in a DataAccessException
        assertThrows(DataAccessException.class, ()-> atDao.insert(bestAuthToken));
    }

    @Test
    public void findPass() throws DataAccessException {
        // Call the find method on atDao, looking for bestAuthToken
        AuthToken compareTest = atDao.find(bestAuthToken.getToken());
        // Determine that a null reference was returned
        assertNull(compareTest);
    }

    @Test
    public void findFail() throws DataAccessException {
        // Close the database connection
        db.closeConnection(false);
        // The find method should not work because the connection is now closed
        assertThrows(DataAccessException.class, ()-> atDao.find(bestAuthToken.getToken()));
        // Reopen the connection so the tearDown() method does not fail
        db.openConnection();
    }

    @Test
    public void deletePass() throws DataAccessException {
        // Call the insert method on the atDao (defined in setUp()) and pass it bestAuthToken (defined in setUp())
        atDao.insert(bestAuthToken);
        // Call the delete method on atDao
        atDao.delete(bestAuthToken.getToken());
        // Call the find method on atDao, looking for bestAuthToken
        AuthToken compareTest = atDao.find(bestAuthToken.getToken());
        // Determine that a null reference was returned
        assertNull(compareTest);
    }

    @Test
    public void deleteNonExistentAuthToken() throws DataAccessException {
        // Call the delete method on atDao
        atDao.delete(bestAuthToken.getToken());
        // Even though bestAuthToken is not in database, delete does not throw an exception
    }

    @Test
    public void clearPass() throws DataAccessException {
        // Call the insert method on the atDao (defined in setUp()) and pass it bestAuthToken (defined in setUp())
        atDao.insert(bestAuthToken);
        // Call the clear method on atDao
        atDao.clear();
        // Call the find method on atDao, looking for bestUser
        AuthToken compareTest = atDao.find(bestAuthToken.getToken());
        // Determine that a null reference was returned
        assertNull(compareTest);
    }
}
