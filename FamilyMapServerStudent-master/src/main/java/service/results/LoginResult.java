package service.results;

public class LoginResult {
    private String authtoken;
    private String username;
    private String personID;
    private String message;
    private boolean success;

    public LoginResult(String authtoken, String username, String personID, String message, boolean success) {
        this.authtoken = authtoken;
        this.username = username;
        this.personID = personID;
        this.message = message;
        this.success = success;
    }
}
