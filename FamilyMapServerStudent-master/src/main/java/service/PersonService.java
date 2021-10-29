package service;

import dao.*;
import model.AuthToken;
import model.Person;
import model.User;
import service.requests.PersonRequest;
import service.results.PersonResult;

/**
 * A service object for the person/[personID] API.
 */
public class PersonService extends Service {

    /**
     * Create a PersonService object.
     */
    public PersonService() {
        db = new Database();
    }

    /**
     * Return a Person object.
     *
     * @param r a PersonRequest object.
     * @return a PersonResult object.
     */
    public PersonResult person(PersonRequest r) {
        try {
            db.openConnection();

            AuthToken authToken = validateAuthToken(r.getAuthToken());

            if (authToken != null) {

                // Get User associated with valid AuthToken
                UserDao uDao = new UserDao(db.getConnection());
                User user = uDao.find(authToken.getUsername());

                // Get PersonID from Request Object and confirm it belongs to current User
                String personID = r.getPersonID();
                if (user.getPersonID().equals(personID)) {

                    // Find person in Database
                    PersonDao pDao = new PersonDao(db.getConnection());
                    Person p = pDao.find(personID);

                    // Close the Database connection
                    db.closeConnection(true);

                    // Confirm Person Object is in Database
                    if (p != null) {
                        return new PersonResult(p.getUsername(), p.getId(), p.getFirstName(), p.getLastName(),
                                                p.getGender(), p.getFatherID(), p.getMotherID(), p.getSpouseID(),
                                                null, true);
                    } else {
                        return new PersonResult("Error: Invalid personID parameter.", false);
                    }

                } else {
                    // Specified personID does not belong to current user
                    db.closeConnection(false);
                    return new PersonResult("Error: Requested person does not belong to this user.",
                                            false);
                }

            } else {
                // The AuthToken is not valid
                db.closeConnection(false);
                return new PersonResult("Error: Invalid auth token.", false);
            }

        } catch (DataAccessException ex) {
            ex.printStackTrace();
            try {
                db.closeConnection(false);
            } catch (DataAccessException e) {
                e.printStackTrace();
            }
            return new PersonResult("Error: Internal server error.", false);
        }
    }
}
