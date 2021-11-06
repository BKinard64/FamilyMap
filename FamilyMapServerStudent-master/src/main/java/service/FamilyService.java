package service;

import dao.*;
import model.AuthToken;
import model.Person;
import results.FamilyResult;

import java.util.List;

/**
 * A service object for the person API.
 */
public class FamilyService extends Service {

    /**
     * Creates a FamilyService object.
     */
    public FamilyService() {
        db = new Database();
    }

    /**
     * Returns all family members of the current User.
     *
     * @return a FamilyResult object.
     */
    public FamilyResult family(String token) {
        try {
            db.openConnection();

            AuthToken authToken = validateAuthToken(token);

            if (authToken != null) {

                // Return the family of the user
                PersonDao pDao = new PersonDao(db.getConnection());
                List<Person> family = pDao.getFamily(authToken.getUsername());
                db.closeConnection(true);
                return new FamilyResult(family, true);

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
