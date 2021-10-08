package service.requests;

import model.Event;
import model.Person;
import model.User;

import java.util.List;

/**
 * A request object for the load API.
 */
public class LoadRequest {
    /**
     * The list of Users to load into the database.
     */
    private List<User> users;
    /**
     * The list of Persons to load into the database.
     */
    private List<Person> persons;
    /**
     * The list of Events to load into the database.
     */
    private List<Event> events;

    /**
     * Create a LoadRequest object.
     *
     * @param users the list of Users to load into the database.
     * @param persons the list of Persons to load into the database.
     * @param events the list of Events to load into the database.
     */
    public LoadRequest(List<User> users, List<Person> persons, List<Event> events) {
        this.users = users;
        this.persons = persons;
        this.events = events;
    }

    public List<User> getUsers() {
        return users;
    }

    public List<Person> getPersons() {
        return persons;
    }

    public List<Event> getEvents() {
        return events;
    }
}
