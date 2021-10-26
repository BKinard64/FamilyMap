package service;

import dao.*;
import model.AuthToken;
import model.Event;
import model.Person;
import model.User;
import service.results.FamilyEventsResult;
import service.results.FamilyResult;

import java.util.List;

/**
 * A service object for the event API.
 */
public class FamilyEventsService {

    /**
     * Create a FamilyEventsService object.
     */
    public FamilyEventsService() {}

    /**
     * Returns every event for each family member of the current User.
     *
     * @return a FamilyEventsResult object.
     */
    public FamilyEventsResult familyEvents(String token) {
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
                    // Return all Events for each Family Member of the above Person
                    EventDao eDao = new EventDao(db.getConnection());
                    List<Event> familyEvents = eDao.getFamilyEvents(person);
                    db.closeConnection(true);
                    return new FamilyEventsResult(familyEvents, true);
                } else {
                    // Person is not in Database
                    throw new DataAccessException("Could not find Person for current User.");
                }
            } else {
                // The AuthToken is not valid
                db.closeConnection(false);
                return new FamilyEventsResult("Error: Invalid auth token.", false);
            }
        } catch (DataAccessException ex) {
            ex.printStackTrace();
            try {
                db.closeConnection(false);
            } catch (DataAccessException e) {
                e.printStackTrace();
            }
            return new FamilyEventsResult("Error: Internal server error.", false);
        }
    }
}
