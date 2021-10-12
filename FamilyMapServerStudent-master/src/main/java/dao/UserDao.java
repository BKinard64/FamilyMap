package dao;

import model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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
    public void insert(User user) throws DataAccessException {
        // Create string to pass into the connection's prepareStatement method
        String sql = "INSERT INTO user (username, pwd, email, first_name, last_name, gender, person_id) " +
                "VALUES(?,?,?,?,?,?,?)";
        // Create a PreparedStatement object using the string above
        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            // Pass the members of the provided User object to the Statement object
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getFirstName());
            stmt.setString(5, user.getLastName());
            stmt.setString(6, user.getGender());
            stmt.setString(7, user.getPersonID());

            // Execute the statement with the passed-in members
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while inserting into the database");
        }
    }

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
