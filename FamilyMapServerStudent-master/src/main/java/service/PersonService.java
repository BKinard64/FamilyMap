package service;

import dao.DataAccessException;
import dao.Database;
import dao.PersonDao;
import model.Person;
import service.requests.PersonRequest;
import service.results.PersonResult;

/**
 * A service object for the person/[personID] API.
 */
public class PersonService {

    /**
     * Create a PersonService object.
     */
    public PersonService() {}

    /**
     * Return a Person object.
     *
     * @param r a PersonRequest object.
     * @return a PersonResult object.
     */
    public PersonResult person(PersonRequest r) {
        Database db = new Database();
        try {
            db.openConnection();
            // Get personID from Request object
            String personID = r.getPersonID();

            // Find person in Database
            PersonDao pDao = new PersonDao(db.getConnection());
            Person p = pDao.find(personID);

            // Close the Database connection
            db.closeConnection(false);

            // Confirm Person Object is not null
            if (p != null) {
                return new PersonResult(p.getUsername(), p.getId(), p.getFirstName(), p.getLastName(), p.getGender(),
                        p.getFatherID(), p.getMotherID(), p.getSpouseID(), null, true);
            } else {
                return new PersonResult(null, null, null, null,
                        null, null, null, null,
                        "Error: Invalid personID parameter.", false);
            }
        } catch (DataAccessException ex) {
            ex.printStackTrace();
            try {
                db.closeConnection(false);
            } catch (DataAccessException e) {
                e.printStackTrace();
            }
            return new PersonResult(null, null, null, null,
                    null, null, null, null,
                    "Error: " + ex.getMessage(), false);
        }
    }
}
