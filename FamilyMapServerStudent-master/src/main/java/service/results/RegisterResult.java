package service.results;

/**
 * A result object for the register API.
 */
public class RegisterResult extends Result {
    /**
     * The AuthToken genearated by a successful register.
     */
    private String authtoken;
    /**
     * The username for the registered User.
     */
    private String username;
    /**
     * The ID of the Person object generated for the registered User.
     */
    private String personID;

    /**
     * Create a RegisterResult object with the following parameters.
     *
     * @param authtoken the AuthToken genearated by a successful register.
     * @param username the username for the registered User.
     * @param personID the ID of the Person object generated for the registered User.
     * @param message the error message in the event of an unsuccessful registration.
     * @param success an indicator of a successful/unsuccessful registration.
     */
    public RegisterResult(String authtoken, String username, String personID, String message, boolean success) {
        this.authtoken = authtoken;
        this.username = username;
        this.personID = personID;
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
