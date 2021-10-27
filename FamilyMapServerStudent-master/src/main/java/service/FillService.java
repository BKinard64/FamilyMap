package service;

import dao.DataAccessException;
import dao.Database;
import dao.UserDao;
import model.User;
import service.requests.FillRequest;
import service.results.FillResult;

/**
 * A service object for the fill API.
 */
public class FillService {

    /**
     * Create a FillService object.
     */
    public FillService() {}

    /**
     * Populate the database with generated data for specified User.
     *
     * @param r a FillRequest object.
     * @return a FillResult object.
     */
    public FillResult fill(FillRequest r) {
        Database db = new Database();
        try {
            db.openConnection();

            // Get username from Request object and validate username
            String username = r.getUsername();
            UserDao uDao = new UserDao(db.getConnection());
            User user = uDao.find(username);

            if (user != null) {

                // Confirm generations parameter is valid
                int generations = Integer.parseInt(r.getGenerations());
                if (generations >= 0) {

                } else {
                    // Generations parameter is negative and therefore invalid
                    throw new NumberFormatException("Generations parameter must be non-negative");
                }
            } else {
                // The username is not valid
                db.closeConnection(false);
                return new FillResult("Error: Invalid username.", false);
            }
        } catch (DataAccessException ex) {
            ex.printStackTrace();
            try {
                db.closeConnection(false);
            } catch (DataAccessException e) {
                e.printStackTrace();
            }
            return new FillResult("Error: Internal server error.", false);
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
            try {
                db.closeConnection(false);
            } catch (DataAccessException e) {
                e.printStackTrace();
            }
            return new FillResult("Error: Invalid generations parameter", false);
        }
        return null;
    }
}
