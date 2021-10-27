package model;

import dao.DataAccessException;
import dao.EventDao;
import dao.PersonDao;
import dao.UserDao;

import java.sql.Connection;
import java.util.UUID;

public class AncestryGenerator {
    private Connection conn;
    private User user;
    private int generations;
    private enum Gender {MALE, FEMALE};
    private Gender userGender;
    private int personsGenerated;
    private int eventsGenerated;

    public AncestryGenerator(Connection conn, User user, int generations) {
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
    }

    public void deleteFamilyData() throws DataAccessException {
        deleteUserData(user.getPersonID());
    }

    private void deleteUserData(String personID) throws DataAccessException {
        if (personID != null) {
            // DELETE EVENTS ASSOCIATED WITH THIS PERSON
            EventDao eDao = new EventDao(conn);
            eDao.deletePersonEvents(personID);

            // DELETE EVENTS ASSOCIATED WITH FAMILY MEMBERS OF THIS PERSON
            PersonDao pDao = new PersonDao(conn);
            Person person = pDao.find(personID);
            if (person != null) {
                deleteUserData(person.getFatherID());
                deleteUserData(person.getMotherID());
                deleteUserData(person.getSpouseID());
            }

            // DELETE THIS PERSON
            pDao.delete(personID);
        }
    }

    public int[] generateFamilyData() throws DataAccessException {
        Person userPerson = generatePerson(userGender, generations, false);
        // Create a new User object for current user with updated PersonID
        User newUser = new User(user.getUsername(), user.getPassword(), user.getEmail(), user.getFirstName(),
                                user.getLastName(), user.getGender(), userPerson.getId());
        // Update User's Person object with original username, first name, and last name
        userPerson.setUsername(newUser.getUsername());
        userPerson.setFirstName(newUser.getFirstName());
        userPerson.setLastName(newUser.getLastName());
        // Remove old User object from Database
        UserDao uDao = new UserDao(conn);
        uDao.delete(user.getUsername());
        // Add new User object to Database
        uDao.insert(newUser);

        return new int[] {personsGenerated, eventsGenerated};
    }

    private Person generatePerson(Gender gender, int generations, boolean deathEvent) throws DataAccessException {
        Person mother = null;
        Person father = null;

        if (generations > 0) {
            mother = generatePerson(Gender.FEMALE, generations - 1, true);
            father = generatePerson(Gender.MALE, generations - 1, true);

            father.setSpouseID(mother.getId());
            mother.setSpouseID(father.getId());

            // DETERMINE YEAR
            generateEvent(father.getId(), "Marriage", 0);
            generateEvent(mother.getId(), "Marriage", 0);
        }

        // Create Person
        Person person = new Person();
        personsGenerated++;
        person.setId(UUID.randomUUID().toString());
        // Set first name
        // Set last name
        if (gender.equals(Gender.MALE)) {
            person.setGender("m");
        } else {
            person.setGender("f");
        }
        if (father != null && mother != null) {
            person.setFatherID(father.getId());
            person.setMotherID(mother.getId());
        }

        // Create Events for Person
        // DETERMINE YEAR
        generateEvent(person.getId(), "Birth", 0);
        if (deathEvent) {
            // DETERMINE YEAR
            generateEvent(person.getId(), "Death", 0);
        }

        // Save person in database
        new PersonDao(conn).insert(person);

        return person;
    }

    private void generateEvent(String personID, String eventType, int year) throws DataAccessException {
        // Create Event
        Event event = new Event();
        eventsGenerated++;
        event.setId(UUID.randomUUID().toString());
        event.setPersonID(personID);
        // Set location data
        event.setType(eventType);
        event.setYear(year);

        // Save event in database
        new EventDao(conn).insert(event);
    }
}
