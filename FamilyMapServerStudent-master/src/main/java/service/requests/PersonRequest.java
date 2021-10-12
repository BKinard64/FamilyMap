package service.requests;

/**
 * A request object for the person/[personID] API.
 */
public class PersonRequest {
    /**
     * The ID of the person to be found.
     */
    private String personID;

    /**
     * Create a PersonRequest Object.
     *
     * @param personID the ID of the person to be found.
     */
    public PersonRequest(String personID) {
        this.personID = personID;
    }

    public String getPersonID() {
        return personID;
    }
}
