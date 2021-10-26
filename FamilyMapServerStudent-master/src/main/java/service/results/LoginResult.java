package service.results;

/**
 * A result object for the login API.
 */
public class LoginResult extends Result {
    /**
     * The generated AuthToken for the User logging in.
     */
    private String authtoken;
    /**
     * The associated username of the User logging in.
     */
    private String username;
    /**
     * The ID of the Person this User is associated with.
     */
    private String personID;

    /**
     * Create a LoginResult object.
     *
     * @param authtoken the generated AuthToken for the User logging in.
     * @param username the associated username for the User logging in.
     * @param personID the ID of the Person this User is associated with.
     * @param message An error message in the event the request fails.
     * @param success An indicator of a successful/unsuccessful login.
     */
    public LoginResult(String authtoken, String username, String personID, String message, boolean success) {
        this.authtoken = authtoken;
        this.username = username;
        this.personID = personID;
        this.message = message;
        this.success = success;
    }

    public LoginResult(String message, boolean success) {
        this.message = message;
        this.success = success;
    }

    public String getAuthtoken() {
        return authtoken;
    }

    public String getUsername() {
        return username;
    }

    public String getPersonID() {
        return personID;
    }
}
