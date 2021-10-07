package dao;

import model.AuthToken;
import model.Person;
import model.Event;

import java.util.List;
import java.sql.Connection;

public class AuthTokenDao {
    private final Connection conn;

    public AuthTokenDao(Connection conn) { this.conn = conn; }

    public List<Person> getFamily(AuthToken token) {return null;}

    public List<Event> getFamilyEvents(AuthToken token) {return null;}

}
