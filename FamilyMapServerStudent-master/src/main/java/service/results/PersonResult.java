package service.results;

/**
 * A result object for the person/[personID] API.
 */
public class PersonResult extends Result {
    /**
     * The username of the Person found.
     */
    private String associatedUsername;
    /**
     * The ID of the Person found.
     */
    private String personID;
    /**
     * The first name of the Person found.
     */
    private String firstName;
    /**
     * The last name of the Person found.
     */
    private String lastName;
    /**
     * The gender of the Person found.
     */
    private String gender;
    /**
     * The ID of the Person's father.
     */
    private String fatherID;
    /**
     * The ID of the Person's mother.
     */
    private String motherID;
    /**
     * The ID of the Person's spouse.
     */
    private String spouseID;

    /**
     * Create a PersonResult object.
     *
     * @param associatedUsername the username of the Person found.
     * @param personID the ID of the Person found.
     * @param firstName the first name of the Person found.
     * @param lastName the last name of the Person found.
     * @param gender the gender of the Person found.
     * @param fatherID the ID of the Person's father.
     * @param motherID the ID of the Person's mother.
     * @param spouseID the ID of the Person's spouse.
     * @param message an error message in the event the request fails.
     * @param success an indicator of the request being successful or not.
     */
    public PersonResult(String associatedUsername, String personID, String firstName, String lastName, String gender,
                        String fatherID, String motherID, String spouseID, String message, boolean success) {
        this.associatedUsername = associatedUsername;
        this.personID = personID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.fatherID = fatherID;
        this.motherID = motherID;
        this.spouseID = spouseID;
        this.message = message;
        this.success = success;
    }

    public PersonResult(String message, boolean success) {
        this.message = message;
        this.success = success;
    }

    public String getAssociatedUsername() {
        return associatedUsername;
    }

    public String getPersonID() {
        return personID;
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

    public String getFatherID() {
        return fatherID;
    }

    public String getMotherID() {
        return motherID;
    }

    public String getSpouseID() {
        return spouseID;
    }
}
