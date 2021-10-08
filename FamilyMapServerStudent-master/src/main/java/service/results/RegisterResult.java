package service.results;

public class RegisterResult {
    private String authtoken;
    private String username;
    private String personID;
    private String message;
    private boolean success;

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

    public String getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return success;
    }
}
