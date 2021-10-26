package service;

import dao.*;
import model.Event;
import model.Person;
import model.User;
import service.requests.LoadRequest;
import service.results.LoadResult;

/**
 * A service object for the load API.
 */
public class LoadService {

    /**
     * Create a LoadService object.
     */
    public LoadService() {}

    /**
     * Load provided Users, Persons, and Events into the database.
     *
     * @param r a LoadRequest object.
     * @return a LoadResult object.
     */
    public LoadResult load(LoadRequest r) {
        Database db = new Database();
        try {
            db.openConnection();

            // Clear the database
            db.clearTables();

            // Load the User data into the Database
            UserDao uDao = new UserDao(db.getConnection());
            for (User u : r.getUsers()) {
                uDao.insert(u);
            }

            // Load the Person data into the Database
            PersonDao pDao = new PersonDao(db.getConnection());
            for (Person p : r.getPersons()) {
                pDao.insert(p);
            }

            // Load the Event data into the Database
            EventDao eDao = new EventDao(db.getConnection());
            for (Event e : r.getEvents()) {
                eDao.insert(e);
            }

            // Close the connection and commit the changes
            db.closeConnection(true);
            return new LoadResult("Successfully added " + r.getUsers().size() + " users, " +
                                    r.getPersons().size() + " persons, and " + r.getEvents().size() +
                                    " events to the database.", true);
        } catch (DataAccessException ex) {
            ex.printStackTrace();
            try {
                db.closeConnection(false);
            } catch (DataAccessException e) {
                e.printStackTrace();
            }
            return new LoadResult("Error: " + ex.getMessage(), false);
        }
    }
}
