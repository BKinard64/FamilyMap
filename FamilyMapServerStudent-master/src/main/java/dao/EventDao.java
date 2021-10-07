package dao;

import model.Event;

import java.sql.Connection;

public class EventDao {
    private final Connection conn;

    public EventDao(Connection conn) {
        this.conn = conn;
    }

    public void insert(Event event) {}

    public Event find(String eventID) {return null;}

    public void delete(Event event) {}
}
