package dao;

import model.User;
import org.junit.jupiter.api.BeforeEach;

import java.sql.Connection;

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
}
