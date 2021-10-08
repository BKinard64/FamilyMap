package model;

import java.util.Objects;

/**
 * A record from the Person table in the database.
 */
public class Person {
    /**
     * The unique identifier for this person.
     */
    private String id;
    /**
     * The username to which this person belongs.
     */
    private String username;
    /**
     * The person's first name.
     */
    private String firstName;
    /**
     * The person's last name.
     */
    private String lastName;
    /**
     * The person's gender.
     */
    private String gender;
    /**
     * The person ID of this person's father.
     */
    private String fatherID;
    /**
     * The person ID of this person's mother.
     */
    private String motherID;
    /**
     * The person ID of this person's spouse.
     */
    private String spouseID;

    /**
     * Creates a person with the following parameters.
     *
     * @param id the person's unique identifier.
     * @param username the person's associated username.
     * @param firstName the person's first name.
     * @param lastName the person's last name.
     * @param gender the person's gender.
     * @param fatherID the unique identifier of the person's father.
     * @param motherID the unique identifier of the person's mother.
     * @param spouseID the unique identifier of the person's spouse.
     */
    public Person(String id, String username, String firstName, String lastName, String gender, String fatherID,
                  String motherID, String spouseID) {
        this.id = id;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.fatherID = fatherID;
        this.motherID = motherID;
        this.spouseID = spouseID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getFatherID() {
        return fatherID;
    }

    public void setFatherID(String fatherID) {
        this.fatherID = fatherID;
    }

    public String getMotherID() {
        return motherID;
    }

    public void setMotherID(String motherID) {
        this.motherID = motherID;
    }

    public String getSpouseID() {
        return spouseID;
    }

    public void setSpouseID(String spouseID) {
        this.spouseID = spouseID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return id.equals(person.id) && Objects.equals(username, person.username) && firstName.equals(person.firstName) &&
                lastName.equals(person.lastName) && gender.equals(person.gender) &&
                Objects.equals(fatherID, person.fatherID) && Objects.equals(motherID, person.motherID) &&
                Objects.equals(spouseID, person.spouseID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, firstName, lastName, gender, fatherID, motherID, spouseID);
    }

    @Override
    public String toString() {
        return "Person{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", gender='" + gender + '\'' +
                ", fatherID='" + fatherID + '\'' +
                ", motherID='" + motherID + '\'' +
                ", spouseID='" + spouseID + '\'' +
                '}';
    }
}
