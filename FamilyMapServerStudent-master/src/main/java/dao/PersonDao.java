package dao;

import model.Person;

import java.sql.Connection;
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
    public void insert(Person person) {}

    /**
     * Find a Person with the given person ID.
     *
     * @param personID the ID of the person to find.
     * @return a Person object corresponding to the given person ID.
     */
    public Person find(String personID) {return null;}

    /**
     * Find the family members of the current user.
     *
     * @param username the username of the current user.
     * @return a list of Person objects representing the family members of the current user.
     */
    public List<Person> getFamily(String username) {return null;}

    /**
     * Remove a Person from the Person table.
     *
     * @param person the Person to remove.
     */
    public void delete(Person person) {}
}
