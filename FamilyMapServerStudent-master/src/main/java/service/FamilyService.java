package service;

import dao.*;
import model.AuthToken;
import model.Person;
import model.User;
import service.results.FamilyResult;

/**
 * A service object for the person API.
 */
public class FamilyService {

    /**
     * Creates a FamilyService object.
     */
    public FamilyService() {}

    /**
     * Returns all family members of the current User.
     *
     * @return a FamilyResult object.
     */
    public FamilyResult family(String token) {
        Database db = new Database();
        try {
            db.openConnection();

            // Validate AuthToken
            AuthTokenDao atDao = new AuthTokenDao(db.getConnection());
            AuthToken authToken = atDao.find(token);

            if (authToken != null) {

                // Get User associated with valid AuthToken
                UserDao uDao = new UserDao(db.getConnection());
                User user = uDao.find(authToken.getUsername());

                // Get Person associated with User
                PersonDao pDao = new PersonDao(db.getConnection());
                Person person = pDao.find(user.getPersonID());

                // Confirm Person is in Database
                if (person != null) {
                    // Return the family of the above person
                    return new FamilyResult(pDao.getFamily(person), true);
                } else {
                    // Person is not in Database
                    throw new DataAccessException("Could not find Person for current User.");
                }
            } else {
                // The AuthToken is not valid
                db.closeConnection(false);
                return new FamilyResult("Error: Invalid auth token.", false);
            }
        } catch (DataAccessException ex) {
            ex.printStackTrace();
            try {
                db.closeConnection(false);
            } catch (DataAccessException e) {
                e.printStackTrace();
            }
            return new FamilyResult("Error: Internal server error.", false);
        }
    }
}
