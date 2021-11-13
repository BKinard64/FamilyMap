package applogic;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import model.Event;
import model.Person;

public class DataCache {
    private static DataCache instance = new DataCache();

    public static DataCache getInstance() {
        return instance;
    }

    private DataCache() {}

    private String authToken;
    private String personID;
    private Map<String, Person> people;
    private Map<String, Event> events;
    private Map<String, List<Event>> personEvents;
    private Set<String> paternalAncestors;
    private Set<String> maternalAncestors;

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public Person getPerson() {
        return this.people.get(this.personID);
    }

    public Map<String, Person> getPeople() {
        return people;
    }

    public void setPeople(List<Person> people) {
        this.people = new HashMap<>();
        for (Person person : people) {
            this.people.put(person.getId(), person);
        }
    }

    public Map<String, Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = new HashMap<>();
        for (Event event : events) {
            this.events.put(event.getId(), event);
        }
    }
}
