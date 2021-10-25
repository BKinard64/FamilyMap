package service;

import dao.DataAccessException;
import dao.Database;
import service.results.ClearResult;

/**
 * A service object for the clear API.
 */
public class ClearService {

    /**
     * Create a ClearService object.
     */
    public ClearService() {}

    /**
     * Remove every record from the database.
     *
     * @return a ClearResult object.
     */
    public ClearResult clear() {
        Database db = new Database();
        try {
            db.openConnection();
            // Clear the database
            db.clearTables();
            // Close the connection and commit the changes
            db.closeConnection(true);
            return new ClearResult("Clear succeeded.", true);
        } catch (DataAccessException ex) {
            ex.printStackTrace();
            try {
                db.closeConnection(false);
            } catch (DataAccessException e) {
                e.printStackTrace();
            }
            return new ClearResult("Error: " + ex.getMessage(), false);
        }
    }
}
