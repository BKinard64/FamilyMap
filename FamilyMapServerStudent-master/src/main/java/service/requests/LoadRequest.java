package service.requests;

import model.Event;
import model.Person;
import model.User;

import java.util.List;

public class LoadRequest {
    private List<User> users;
    private List<Person> persons;
    private List<Event> events;

    public LoadRequest(List<User> users, List<Person> persons, List<Event> events) {
        this.users = users;
        this.persons = persons;
        this.events = events;
    }
}
