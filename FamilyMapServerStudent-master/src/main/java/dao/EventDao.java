package dao;

import model.Event;

import java.sql.*;
import java.util.ArrayList;
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
     * @param event the Event to insert.
     * @throws DataAccessException thrown if SQLException occurs
     */
    public void insert(Event event) throws DataAccessException {
        // Create string to pass into the connection's prepareStatement method
        String sql = "INSERT INTO event (id, username, person_id, latitude, longitude, country, city, type, year) " +
                "VALUES(?,?,?,?,?,?,?,?,?)";
        // Create a PreparedStatement object using the string above
        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            // Pass the members of the provided User object to the Statement object
            stmt.setString(1, event.getId());
            stmt.setString(2, event.getUsername());
            stmt.setString(3, event.getPersonID());
            stmt.setDouble(4, event.getLatitude());
            stmt.setDouble(5, event.getLongitude());
            stmt.setString(6, event.getCountry());
            stmt.setString(7, event.getCity());
            stmt.setString(8, event.getType());
            stmt.setInt(9, event.getYear());

            // Execute the statement with the passed-in members
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while inserting into the database");
        }
    }

    /**
     * Find an Event with the given event ID.
     *
     * @param eventID the ID of the Event to find.
     * @throws DataAccessException thrown if SQLException occurs
     * @return an Event object with the corresponding event ID.
     */
    public Event find(String eventID) throws DataAccessException {
        Event event;
        ResultSet rs = null;
        String sql = "SELECT * FROM event WHERE id = ?;";
        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, eventID);
            rs = stmt.executeQuery();
            if (rs.next()) {
                event = new Event(rs.getString("id"), rs.getString("username"),
                        rs.getString("person_id"), rs.getFloat("latitude"),
                        rs.getFloat("longitude"), rs.getString("country"),
                        rs.getString("city"), rs.getString("type"),
                        rs.getInt("year"));
                return event;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding event");
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
     * Find all the Events of every family member of the current User.
     *
     * @param username the username associated with the current User.
     * @throws DataAccessException thrown if SQLException occurs
     * @return a list of Event objects representing the current User's family member's events.
     */
    public List<Event> getFamilyEvents(String username) throws DataAccessException {
        List<Event> familyEvents = new ArrayList<>();
        ResultSet rs = null;
        String sql = "SELECT * FROM event WHERE username = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            rs = stmt.executeQuery();
            while (rs.next()) {
                Event event = new Event(rs.getString("id"), rs.getString("username"),
                        rs.getString("person_id"), rs.getFloat("latitude"),
                        rs.getFloat("longitude"), rs.getString("country"),
                        rs.getString("city"), rs.getString("type"),
                        rs.getInt("year"));
                familyEvents.add(event);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding user's family's events");
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return familyEvents;
    }

    /**
     * Remove an Event from the Event table.
     *
     * @param eventID the unique ID of the Event to remove.
     * @throws DataAccessException thrown if SQLException occurs
     */
    public void delete(String eventID) throws DataAccessException {
        String sql = "DELETE FROM event WHERE id = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, eventID);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while deleting event from table");
        }
    }

    /**
     * Remove all events associated with specified User
     *
     * @param username the username specifying which User's family events to remove from the database
     * @throws DataAccessException thrown if SQLException occurs
     */
    public void deleteFamilyEvents(String username) throws DataAccessException {
        String sql = "DELETE FROM event WHERE username = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    /**
     * Delete all records from the Event table.
     *
     * @throws DataAccessException thrown if SQLException occurs
     */
    public void clear() throws DataAccessException {
        try (Statement stmt = conn.createStatement()){
            String sql = "DELETE FROM event";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while clearing event table");
        }
    }
}
