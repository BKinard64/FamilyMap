package service;

import dao.*;
import model.AuthToken;
import model.Event;
import results.FamilyEventsResult;

import java.util.List;

/**
 * A service object for the event API.
 */
public class FamilyEventsService extends Service {

    /**
     * Create a FamilyEventsService object.
     */
    public FamilyEventsService() {
        db = new Database();
    }

    /**
     * Returns every event for each family member of the current User.
     *
     * @return a FamilyEventsResult object.
     */
    public FamilyEventsResult familyEvents(String token) {
        try {
            db.openConnection();

            AuthToken authToken = validateAuthToken(token);

            if (authToken != null) {

                // Return all Events for each Family Member of the above Person
                EventDao eDao = new EventDao(db.getConnection());
                List<Event> familyEvents = eDao.getFamilyEvents(authToken.getUsername());
                db.closeConnection(true);
                return new FamilyEventsResult(familyEvents, true);

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
