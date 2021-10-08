package service.requests;

/**
 * A request object for the register API.
 */
public class RegisterRequest {
    /**
     * The username to register as a new user.
     */
    private String username;
    /**
     * The password for the user to register.
     */
    private String password;
    /**
     * The email address for the user to register.
     */
    private String email;
    /**
     * The first name of the user registering.
     */
    private String firstName;
    /**
     *  The last name of the user registering.
     */
    private String lastName;
    /**
     * The gender of the user registering.
     */
    private String gender;

    /**
     * Create a RegisterRequest object.
     *
     * @param username the username to register as a new user.
     * @param password the password for the user to register.
     * @param email the email address of the user registering.
     * @param firstName the first name of the user registering.
     * @param lastName the last name of the user registering.
     * @param gender the gender of the user registering.
     */
    public RegisterRequest(String username, String password, String email, String firstName, String lastName, String gender) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getGender() {
        return gender;
    }
}
