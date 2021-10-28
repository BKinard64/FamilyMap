package model;

import dao.DataAccessException;
import dao.EventDao;
import dao.PersonDao;
import dao.UserDao;
import jsondata.*;

import java.sql.Connection;
import java.util.Random;
import java.util.UUID;

public class AncestryGenerator {
    private Connection conn;
    private User user;
    private int generations;
    private enum Gender {MALE, FEMALE};
    private Gender userGender;
    private int personsGenerated;
    private int eventsGenerated;
    private LocationData locData;
    private FemaleNames fmlNames;
    private MaleNames mlNames;
    private Surnames srNames;

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

    public void deleteFamilyData() throws DataAccessException {
        new EventDao(conn).deleteFamilyEvents(user.getUsername());
        new PersonDao(conn).deleteFamily(user.getUsername());
    }

    public int[] generateFamilyData() throws DataAccessException {
        Person userPerson = generatePerson(userGender, generations, false, 2000,
                                            2080, 1995);
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

        // EDIT USER'S EVENT DATA

        return new int[] {personsGenerated, eventsGenerated};
    }

    private Person generatePerson(Gender gender, int generations, boolean deathEvent,
                                  int birthYear, int deathYear, int marriageYear) throws DataAccessException {
        Person mother = null;
        Person father = null;

        if (generations > 0) {
            mother = generatePerson(Gender.FEMALE, generations - 1, true,
                                    birthYear - 25, deathYear - 25, marriageYear - 25);
            father = generatePerson(Gender.MALE, generations - 1, true,
                                    birthYear - 25, deathYear - 25, marriageYear - 25);

            father.setSpouseID(mother.getId());
            mother.setSpouseID(father.getId());

            Random random = new Random();
            int index = random.nextInt(locData.data.length);
            Location location = locData.data[index];

            generateEvent(father.getId(), "Marriage", marriageYear, location);
            generateEvent(mother.getId(), "Marriage", marriageYear, location);
        }

        // Create Person
        Person person = new Person();
        personsGenerated++;
        person.setId(UUID.randomUUID().toString());
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
        int index = random.nextInt(srNames.data.length);
        person.setLastName(srNames.data[index]);
        if (father != null && mother != null) {
            person.setFatherID(father.getId());
            person.setMotherID(mother.getId());
        }

        // Create Events for Person
        generateEvent(person.getId(), "Birth", birthYear, null);
        if (deathEvent) {
            generateEvent(person.getId(), "Death", deathYear, null);
        }

        // Save person in database
        new PersonDao(conn).insert(person);

        return person;
    }

    private void generateEvent(String personID, String eventType, int year,
                               Location location) throws DataAccessException {
        // Create Event
        Event event = new Event();
        eventsGenerated++;
        event.setId(UUID.randomUUID().toString());
        event.setPersonID(personID);
        if (location == null) {
            Random random = new Random();
            int index = random.nextInt(locData.data.length);
            location = locData.data[index];
        }
        event.setLatitude(location.getLatitude());
        event.setLongitude(location.getLongitude());
        event.setCountry(location.getCountry());
        event.setCity(location.getCity());
        event.setType(eventType);
        event.setYear(year);

        // Save event in database
        new EventDao(conn).insert(event);
    }
}
