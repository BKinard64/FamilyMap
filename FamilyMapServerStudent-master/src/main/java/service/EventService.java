package service;

import dao.*;
import model.AuthToken;
import model.Event;
import model.User;
import requests.EventRequest;
import results.EventResult;

/**
 * A service object for the event/[eventID] API.
 */
public class EventService extends Service {

    /**
     * Create an EventService object.
     */
    public EventService() {
        db = new Database();
    }

    /**
     * Returns the single Event object with the provided event ID.
     *
     * @param r an EventRequest object.
     * @return an EventResult object.
     */
    public EventResult event(EventRequest r) {
        try {
            db.openConnection();

            AuthToken authToken = validateAuthToken(r.getAuthToken());

            if (authToken != null) {

                // Get User associated with valid AuthToken
                UserDao uDao = new UserDao(db.getConnection());
                User user = uDao.find(authToken.getUsername());

                // Get Event belonging to the ID in the Request
                EventDao eDao = new EventDao(db.getConnection());
                Event e = eDao.find(r.getEventID());

                // Confirm the Event is in the Database
                if (e != null) {

                    // Confirm the Event belongs to the current User
                    if (user.getPersonID().equals(e.getPersonID())) {
                        db.closeConnection(true);
                        return new EventResult(e.getUsername(), e.getId(), e.getPersonID(), e.getCountry(),
                                                e.getCity(), e.getType(), e.getLatitude(), e.getLongitude(),
                                                e.getYear(), true);
                    } else {
                        // The specified eventID does not belong to the current User
                        db.closeConnection(false);
                        return new EventResult("Error: Requested event does not belong to this user.",
                                                false);
                    }
                } else {
                    // The eventID does not belong to a valid Event in the Database
                    db.closeConnection(false);
                    return new EventResult("Error: Invalid eventID parameter.", false);
                }
            } else {
                // The AuthToken is not valid
                db.closeConnection(false);
                return new EventResult("Error: Invalid auth token.", false);
            }
        } catch (DataAccessException ex) {
            ex.printStackTrace();
            try {
                db.closeConnection(false);
            } catch (DataAccessException e) {
                e.printStackTrace();
            }
            return new EventResult("Error: Internal server error.", false);
        }
    }
}
