package dao;

import model.Person;

import java.sql.Connection;

public class PersonDao {
    private final Connection conn;

    public PersonDao(Connection conn) {
        this.conn = conn;
    }

    public Person find(String personID) {return null;}


}
