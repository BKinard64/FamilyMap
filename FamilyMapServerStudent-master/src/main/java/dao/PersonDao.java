package dao;

import model.Person;

import java.sql.Connection;
import java.util.List;

public class PersonDao {
    private final Connection conn;

    public PersonDao(Connection conn) {
        this.conn = conn;
    }

    public void insert(Person person) {}

    public Person find(String personID) {return null;}

    public List<Person> getFamily(String username) {return null;}

    public void delete(Person person) {}
}
