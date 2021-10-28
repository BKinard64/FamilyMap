package dao;

import model.Event;
import model.Person;

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
     * @throws DataAccessException
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
     * @throws DataAccessException
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
                        rs.getString("person_id"), rs.getDouble("latitude"),
                        rs.getDouble("longitude"), rs.getString("country"),
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
     * Get all the Events of the given Person
     *
     * @param personID the unique ID of the Person
     * @return a List of Events associated with the Person
     * @throws DataAccessException
     */
    public List<Event> getPersonEvents(String personID) throws DataAccessException {
        List<Event> personEvents = new ArrayList<>();
        ResultSet rs = null;
        String sql = "SELECT * FROM event WHERE person_id = ?;";
        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, personID);
            rs = stmt.executeQuery();
            while (rs.next()) {
                Event event = new Event(rs.getString("id"), rs.getString("username"),
                        rs.getString("person_id"), rs.getDouble("latitude"),
                        rs.getDouble("longitude"), rs.getString("country"),
                        rs.getString("city"), rs.getString("type"),
                        rs.getInt("year"));
                personEvents.add(event);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding person's events");
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return personEvents;
    }

    /**
     * Find all the Events of every family member of the current User.
     *
     * @param p the Person object associated with the current User.
     * @throws DataAccessException
     * @return a list of Event objects representing the current User's family member's events.
     */
    public List<Event> getFamilyEvents(Person p) throws DataAccessException {
        // Create the List Object to return
        List<Event> familyEvents;

        // Get the events of the current User
        familyEvents = getPersonEvents(p.getId());

        // Find the Person Objects associated with the current User's spouse, father, and mother
        PersonDao pDao = new PersonDao(conn);
        Person spouse = pDao.find(p.getSpouseID());
        Person father = pDao.find(p.getFatherID());
        Person mother = pDao.find(p.getMotherID());

        // Add the Events of the User's spouse, mother, and father to the returning list
        if (spouse != null) {
            List<Event> spouseEvents = getPersonEvents(spouse.getId());
            familyEvents.addAll(spouseEvents);
        }
        if (mother != null) {
            List<Event> motherEvents = getPersonEvents(mother.getId());
            familyEvents.addAll(motherEvents);
        }
        if (father != null) {
            List<Event> fatherEvents = getPersonEvents(father.getId());
            familyEvents.addAll(fatherEvents);
        }

        // Add the Events of the parents of the Current User's mother, father, and spouse to the list
        if (mother != null) {
            getParentEvents(mother, pDao, familyEvents);
        }
        if (father != null) {
            getParentEvents(father, pDao, familyEvents);
        }
        if (spouse != null) {
            getParentEvents(spouse, pDao, familyEvents);
        }

        return familyEvents;
    }

    /**
     * Find the events of a given Person's parents
     *
     * @param p the Person
     * @param pDao a Person Data-Access Object to find the Person's parents
     * @param familyEvents a List of family Events to add to
     * @throws DataAccessException
     */
    private void getParentEvents(Person p, PersonDao pDao, List<Event> familyEvents) throws DataAccessException {
        // Find the Person Objects associated with this Person's mother and father
        Person mother = pDao.find(p.getMotherID());
        Person father = pDao.find(p.getFatherID());

        // If this Person has a mother or father in the database, add their Events to the List
        if (mother != null) {
            List<Event> motherEvents = getPersonEvents(mother.getId());
            familyEvents.addAll(motherEvents);
            // Add the Events of the mother's parents to the list
            getParentEvents(mother, pDao, familyEvents);
        }
        if (father != null) {
            List<Event> fatherEvents = getPersonEvents(father.getId());
            familyEvents.addAll(fatherEvents);
            // Add the Events of the father's parents to the list
            getParentEvents(father, pDao, familyEvents);
        }
    }

    /**
     * Remove an Event from the Event table.
     *
     * @param eventID the unique ID of the Event to remove.
     * @throws DataAccessException
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
     * Remove all Events associated with a Person
     *
     * @param personID
     * @throws DataAccessException
     */
    public void deletePersonEvents(String personID) throws DataAccessException {
        String sql = "DELETE FROM event WHERE personID = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, personID);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while deleting persons events.");
        }
    }

    /**
     * Delete all records from the Event table.
     *
     * @throws DataAccessException
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
