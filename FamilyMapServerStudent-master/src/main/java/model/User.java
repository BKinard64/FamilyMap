package model;

import java.util.Objects;

/**
 * A record from the User table in the database.
 */
public class User {
    /**
     * The User's unique username.
     */
    private String username;
    /**
     * The User's password.
     */
    private String password;
    /**
     * The User's email address.
     */
    private String email;
    /**
     * The User's first name.
     */
    private String firstName;
    /**
     * The User's last name.
     */
    private String lastName;
    /**
     * The User's gender.
     */
    private String gender;
    /**
     * The unique Person ID assigned to this User's generated Person object.
     */
    private String personID;

    /**
     * Creates a User with the following parameters.
     *
     * @param username the User's username.
     * @param password the User's password.
     * @param email the User's email address.
     * @param firstName the User's first name.
     * @param lastName the User's last name.
     * @param gender the User's gender.
     * @param personID the unique Person ID for this User.
     */
    public User(String username, String password, String email, String firstName, String lastName, String gender,
                String personID) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.personID = personID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return username.equals(user.username) && password.equals(user.password) && email.equals(user.email) &&
                firstName.equals(user.firstName) && lastName.equals(user.lastName) && gender.equals(user.gender) &&
                personID.equals(user.personID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password, email, firstName, lastName, gender, personID);
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", gender='" + gender + '\'' +
                ", personID='" + personID + '\'' +
                '}';
    }
}
