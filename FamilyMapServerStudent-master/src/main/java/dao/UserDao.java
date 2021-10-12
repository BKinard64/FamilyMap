package dao;

import model.User;

import java.sql.Connection;

/**
 * A data-access object for the User table.
 */
public class UserDao {
    /**
     * A connection to the database.
     */
    private final Connection conn;

    /**
     * Create a UserDao with a connection.
     *
     * @param conn the connection to the database.
     */
    public UserDao(Connection conn) {
        this.conn = conn;
    }

    /**
     * Insert a User into the User table.
     *
     * @param user the User to be inserted into the table.
     */
    public void insert(User user) {}

    /**
     * Find the User with the given username.
     *
     * @param username the username being searched for.
     * @return a User object with the provided username.
     */
    public User find(String username) {return null;}

    /**
     * Remove the User from the User table.
     *
     * @param user the User to remove
     */
    public void delete(User user) {}
}
