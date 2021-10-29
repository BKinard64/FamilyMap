package model;

import dao.DataAccessException;
import dao.EventDao;
import dao.PersonDao;
import dao.UserDao;
import jsondata.*;

import java.sql.Connection;
import java.util.Random;
import java.util.UUID;

/**
 * A class for generating ancestor data for a given User.
 */
public class AncestryGenerator {
    /**
     * An enumerated type representing gender
     */
    private enum Gender {MALE, FEMALE}

    /**
     * A connection to the database
     */
    private final Connection conn;
    /**
     * The User to generate ancestor data for
     */
    private final User user;
    /**
     * The number of generations of ancestor data to create
     */
    private final int generations;
    /**
     * The gender of the User
     */
    private final Gender userGender;
    /**
     * Count of person's generated
     */
    private int personsGenerated;
    /**
     * Count of event's generated
     */
    private int eventsGenerated;
    /**
     * List of locations to use for generated events
     */
    private final LocationData locData;
    /**
     * List of female names to use for generated persons
     */
    private final FemaleNames fmlNames;
    /**
     * List of male names to use for generated persons
     */
    private final MaleNames mlNames;
    /**
     * List of surnames to use for generated persons
     */
    private final Surnames srNames;

    /**
     * Creates an AncestryGenerator with the following parameters
     *
     * @param conn a connection to the database
     * @param user the User to generate ancestor data for
     * @param generations the number of generations of ancestor data to create
     * @param locData list of locations to use for generated events
     * @param fmlNames list of female names to use for generated persons
     * @param mlNames list of male names to use for generated persons
     * @param srNames list of surnames to use for generated persons
     */
    public AncestryGenerator(Connection conn, User user, int generations, LocationData locData, FemaleNames fmlNames,
                             MaleNames mlNames, Surnames srNames) {
        this.conn = conn;
        this.user = user;
        this.generations = generations;
        if (user.getGender().equals("m")) {
            this.userGender = Gender.MALE;
        } else {
            this.userGender = Gender.FEMALE;
        }
        this.personsGenerated = 0;
        this.eventsGenerated = 0;
        this.locData = locData;
        this.fmlNames = fmlNames;
        this. mlNames = mlNames;
        this.srNames = srNames;
    }

    /**
     * Deletes all person and event data related to User
     *
     * @throws DataAccessException thrown if EventDao or PersonDao encounters error
     */
    public void deleteFamilyData() throws DataAccessException {
        new EventDao(conn).deleteFamilyEvents(user.getUsername());
        new PersonDao(conn).deleteFamily(user.getUsername());
    }

    /**
     * Generates ancestor data for User
     *
     * @return an integer array representing the number of persons and events generated
     * @throws DataAccessException thrown if EventDao or PersonDao encounters error
     */
    public int[] generateFamilyData() throws DataAccessException {
        Person userPerson = generatePerson(userGender, generations, false, 2000,
                                            2080, 1995);

        // Create a new User object for current user with updated PersonID
        User newUser = new User(user.getUsername(), user.getPassword(), user.getEmail(), user.getFirstName(),
                                user.getLastName(), user.getGender(), userPerson.getId());
        // Update User's Person object with original first name, and last name
        userPerson.setFirstName(newUser.getFirstName());
        userPerson.setLastName(newUser.getLastName());
        // Remove old User object from Database
        UserDao uDao = new UserDao(conn);
        uDao.delete(user.getUsername());
        // Add new User object to Database
        uDao.insert(newUser);

        // Add Person object to database
        PersonDao pDao = new PersonDao(conn);
        pDao.insert(userPerson);

        return new int[] {personsGenerated, eventsGenerated};
    }

    /**
     * Generate a person associated with the User
     *
     * @param gender the gender of this person
     * @param generations the number of generations still to be created
     * @param deathEvent whether to create a death event for this person
     * @param birthYear the year this person should be born
     * @param deathYear the year this person should die
     * @param marriageYear the year this person's parents should be married
     *
     * @return the Person generated
     * @throws DataAccessException thrown if EventDao or PersonDao encounters error
     */
    private Person generatePerson(Gender gender, int generations, boolean deathEvent,
                                  int birthYear, int deathYear, int marriageYear) throws DataAccessException {
        Person mother = null;
        Person father = null;

        if (generations > 0) {
            mother = generatePerson(Gender.FEMALE, generations - 1, true,
                                    birthYear - 25, deathYear - 25, marriageYear - 25);
            father = generatePerson(Gender.MALE, generations - 1, true,
                                    birthYear - 25, deathYear - 25, marriageYear - 25);

            // Set father and mother's spouseID's to each other
            father.setSpouseID(mother.getId());
            mother.setSpouseID(father.getId());

            // Add father and mother to database
            PersonDao pDao = new PersonDao(conn);
            pDao.insert(mother);
            pDao.insert(father);

            // Get location data for father and mother's marriage event
            Random random = new Random();
            int index = random.nextInt(locData.data.length);
            Location location = locData.data[index];

            // Create father and mother's marriage event
            generateEvent(father.getId(), "Marriage", marriageYear, location);
            generateEvent(mother.getId(), "Marriage", marriageYear, location);
        }

        // Create Person
        Person person = setPersonFields(gender, father, mother);

        // Create Events for Person
        generateEvent(person.getId(), "Birth", birthYear, null);
        if (deathEvent) {
            generateEvent(person.getId(), "Death", deathYear, null);
        }

        return person;
    }

    /**
     * Generate an event for the User
     *
     * @param personID the personID this event belongs to
     * @param eventType the type of the event to generate
     * @param year the year of the event
     * @param location the location data for the event
     *
     * @throws DataAccessException thrown if EventDao or PersonDao encounters error
     */
    private void generateEvent(String personID, String eventType, int year,
                               Location location) throws DataAccessException {
        Event event = new Event();
        eventsGenerated++;

        // Set eventID, username, and personID
        event.setId(UUID.randomUUID().toString());
        event.setUsername(user.getUsername());
        event.setPersonID(personID);

        // If location details are not provided, grab them from locData
        if (location == null) {
            Random random = new Random();
            int index = random.nextInt(locData.data.length);
            location = locData.data[index];
        }

        // Set location data
        event.setLatitude(location.getLatitude());
        event.setLongitude(location.getLongitude());
        event.setCountry(location.getCountry());
        event.setCity(location.getCity());

        // Set eventType and year
        event.setType(eventType);
        event.setYear(year);

        // Save event in database
        new EventDao(conn).insert(event);
    }

    /**
     * Create a Person object and set its fields
     *
     * @param gender the gender to set for the Person
     * @param father the Person object representing this Person's father
     * @param mother the Person object representing this Person's mother
     * @return the Person object created
     */
    private Person setPersonFields(Gender gender, Person father, Person mother) {
        Person person = new Person();
        personsGenerated++;

        // Set personID and username
        person.setId(UUID.randomUUID().toString());
        person.setUsername(user.getUsername());

        // Set male/female gender and first name
        Random random = new Random();
        if (gender.equals(Gender.MALE)) {
            person.setGender("m");
            int index = random.nextInt(mlNames.data.length);
            person.setFirstName(mlNames.data[index]);
        } else {
            person.setGender("f");
            int index = random.nextInt(fmlNames.data.length);
            person.setFirstName(fmlNames.data[index]);
        }

        // Set last name
        int index = random.nextInt(srNames.data.length);
        person.setLastName(srNames.data[index]);

        // Set father and mother IDs if appropriate
        if (father != null && mother != null) {
            person.setFatherID(father.getId());
            person.setMotherID(mother.getId());
        }

        return person;
    }
}
