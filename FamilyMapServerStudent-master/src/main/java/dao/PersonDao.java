package dao;

import model.Person;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * A data-access object for the Person table.
 */
public class PersonDao {
    /**
     * A connection to the database.
     */
    private final Connection conn;

    /**
     * Create a PersonDao with a connection.
     *
     * @param conn the connection to the database.
     */
    public PersonDao(Connection conn) {
        this.conn = conn;
    }

    /**
     * Insert a Person into the Person table.
     *
     * @param person the Person object to be inserted.
     */
    public void insert(Person person) throws DataAccessException {
        // Create string to pass into the connection's prepareStatement method
        String sql = "INSERT INTO person (id, username, first_name, last_name, gender, father_id, " +
                "mother_id, spouse_id) VALUES(?,?,?,?,?,?,?,?)";
        // Create a PreparedStatement object using the string above
        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            // Pass the members of the provided Person object to the Statement object
            stmt.setString(1, person.getId());
            stmt.setString(2, person.getUsername());
            stmt.setString(3, person.getFirstName());
            stmt.setString(4, person.getLastName());
            stmt.setString(5, person.getGender());
            stmt.setString(6, person.getFatherID());
            stmt.setString(7, person.getMotherID());
            stmt.setString(8, person.getSpouseID());

            // Execute the statement with the passed-in members
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while inserting into the database");
        }
    }

    /**
     * Find a Person with the given person ID.
     *
     * @param personID the ID of the Person to find.
     * @return a Person object corresponding to the given person ID.
     */
    public Person find(String personID) {return null;}

    /**
     * Find the family members of the current user.
     *
     * @param username the username of the current User.
     * @return a list of Person objects representing the family members of the current User.
     */
    public List<Person> getFamily(String username) {return null;}

    /**
     * Remove a Person from the Person table.
     *
     * @param person the Person to remove.
     */
    public void delete(Person person) {}

    /**
     * Delete all records from the Person table
     *
     * @throws DataAccessException
     */
    public void clear() throws DataAccessException {
        try (Statement stmt = conn.createStatement()){
            String sql = "DELETE FROM person";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new DataAccessException("SQL Error encountered while clearing person table");
        }
    }
}
