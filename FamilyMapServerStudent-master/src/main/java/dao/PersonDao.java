package dao;

import model.Person;

import java.sql.*;
import java.util.ArrayList;
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
     * @throws DataAccessException thrown if SQLException occurs
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
     * @throws DataAccessException thrown if SQLException occurs
     * @return a Person object corresponding to the given person ID.
     */
    public Person find(String personID) throws DataAccessException {
        Person person;
        ResultSet rs = null;
        String sql = "SELECT * FROM person WHERE id = ?;";
        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, personID);
            rs = stmt.executeQuery();
            if (rs.next()) {
                person = new Person(rs.getString("id"), rs.getString("username"),
                        rs.getString("first_name"), rs.getString("last_name"),
                        rs.getString("gender"), rs.getString("father_id"),
                        rs.getString("mother_id"), rs.getString("spouse_id"));
                return person;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding person");
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * Find the family members of the current user.
     *
     * @param username the username associated with the current User.
     * @throws DataAccessException thrown if SQLException occurs
     * @return a list of Person objects representing the family members of the current User.
     */
    public List<Person> getFamily(String username) throws DataAccessException {
        List<Person> family = new ArrayList<>();
        ResultSet rs = null;
        String sql = "SELECT * FROM person WHERE username = ?;";
        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            rs = stmt.executeQuery();
            while (rs.next()) {
                Person person = new Person(rs.getString("id"), rs.getString("username"),
                                            rs.getString("first_name"), rs.getString("last_name"),
                                            rs.getString("gender"), rs.getString("father_id"),
                                            rs.getString("mother_id"), rs.getString("spouse_id"));
                family.add(person);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding user's family");
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return family;
    }

    /**
     * Remove a Person from the Person table.
     *
     * @param personID the ID of the Person to remove.
     * @throws DataAccessException thrown if SQLException occurs
     */
    public void delete(String personID) throws DataAccessException {
        String sql = "DELETE FROM person WHERE id = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, personID);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while deleting person from table");
        }
    }

    /**
     * Remove the family members of specified User from the database
     *
     * @param username the username specifying which User's family to remove from database
     * @throws DataAccessException thrown if SQLException occurs
     */
    public void deleteFamily(String username) throws DataAccessException {
        String sql = "DELETE FROM person WHERE username = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while deleting user's family from person table");
        }
    }

    /**
     * Delete all records from the Person table
     *
     * @throws DataAccessException thrown if SQLException occurs
     */
    public void clear() throws DataAccessException {
        try (Statement stmt = conn.createStatement()){
            String sql = "DELETE FROM person";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while clearing person table");
        }
    }
}
