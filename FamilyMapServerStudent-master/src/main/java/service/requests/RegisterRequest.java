package service.requests;

/**
 * A request object for the register API.
 */
public class RegisterRequest {
    /**
     * The username to register as a new User.
     */
    private String username;
    /**
     * The password for the User to register.
     */
    private String password;
    /**
     * The email address for the User to register.
     */
    private String email;
    /**
     * The first name of the User registering.
     */
    private String firstName;
    /**
     *  The last name of the User registering.
     */
    private String lastName;
    /**
     * The gender of the User registering.
     */
    private String gender;

    /**
     * Create a RegisterRequest object.
     *
     * @param username the username to register as a new User.
     * @param password the password for the User to register.
     * @param email the email address of the User registering.
     * @param firstName the first name of the User registering.
     * @param lastName the last name of the User registering.
     * @param gender the gender of the User registering.
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
