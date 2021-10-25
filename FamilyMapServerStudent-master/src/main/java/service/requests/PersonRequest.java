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
     * The authToken sent with the request.
     */
    private String authToken;

    /**
     * Create a PersonRequest Object.
     *
     * @param personID the ID of the person to be found.
     * @param authToken the authToken sent with the request.
     */
    public PersonRequest(String personID, String authToken) {
        this.personID = personID;
        this.authToken = authToken;
    }

    public String getPersonID() {
        return personID;
    }

    public String getAuthToken() { return authToken; }
}
