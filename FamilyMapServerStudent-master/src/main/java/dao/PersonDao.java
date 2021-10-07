package dao;

import model.AuthToken;
import model.Person;


import java.sql.Connection;

public class PersonDao {
    private final Connection conn;

    public PersonDao(Connection conn) {
        this.conn = conn;
    }

    public void insert(Person person) {}

    public Person find(String personID) {return null;}

    public void delete(Person person) {}
}
