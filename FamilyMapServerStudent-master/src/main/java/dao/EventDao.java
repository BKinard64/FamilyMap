package dao;

import model.Event;

import java.sql.Connection;
import java.util.List;

public class EventDao {
    private final Connection conn;

    public EventDao(Connection conn) {
        this.conn = conn;
    }

    public void insert(Event event) {}

    public Event find(String eventID) {return null;}

    public List<Event> getFamilyEvents(String username) {return null;}

    public void delete(Event event) {}
}
