package service.requests;

/**
 * A request object for the login API.
 */
public class LoginRequest {
    /**
     * The username of the User logging in.
     */
    private String username;
    /**
     * The password of the User logging in.
     */
    private String password;

    /**
     * Create a LoginRequest object.
     *
     * @param username the username of the User logging in.
     * @param password the password of the User logging in.
     */
    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
