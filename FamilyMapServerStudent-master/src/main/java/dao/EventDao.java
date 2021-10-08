package dao;

import model.Event;

import java.sql.Connection;
import java.util.List;

/**
 * A data-access object for the Event table.
 */
public class EventDao {
    /**
     * A connection to the database.
     */
    private final Connection conn;

    /**
     * Create an EventDao with a connection.
     *
     * @param conn a connection to the database.
     */
    public EventDao(Connection conn) {
        this.conn = conn;
    }

    /**
     * Insert an Event to the Event table.
     *
     * @param event the event to insert.
     */
    public void insert(Event event) {}

    /**
     * Find an Event with the given event ID.
     *
     * @param eventID the ID of the event to find.
     * @return an Event object with the corresponding event ID.
     */
    public Event find(String eventID) {return null;}

    /**
     * Find the all the events of every family member of the current user.
     *
     * @param username the username of the current user.
     * @return a list of Event objects representing the current user's family member's events.
     */
    public List<Event> getFamilyEvents(String username) {return null;}

    /**
     * Remove an event from the Event table.
     *
     * @param event the event to remove.
     */
    public void delete(Event event) {}
}
