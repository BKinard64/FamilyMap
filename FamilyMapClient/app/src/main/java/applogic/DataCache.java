package applogic;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import java.util.ArrayList;
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
    private Map<String, Float> eventColors;
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

    public Map<String, Float> getEventColors() {
        return eventColors;
    }

    public void setEventColors() {
        // Create a list of all the available marker colors
        List<Float> colors = new ArrayList<>();
        colors.add(BitmapDescriptorFactory.HUE_BLUE);
        colors.add(BitmapDescriptorFactory.HUE_RED);
        colors.add(BitmapDescriptorFactory.HUE_GREEN);
        colors.add(BitmapDescriptorFactory.HUE_YELLOW);
        colors.add(BitmapDescriptorFactory.HUE_VIOLET);
        colors.add(BitmapDescriptorFactory.HUE_ORANGE);
        colors.add(BitmapDescriptorFactory.HUE_CYAN);
        colors.add(BitmapDescriptorFactory.HUE_ROSE);
        colors.add(BitmapDescriptorFactory.HUE_AZURE);
        colors.add(BitmapDescriptorFactory.HUE_MAGENTA);

        // Assign each unique eventType a unique color
        eventColors = new HashMap<>();
        int colorIndex = 0;
        for (Event event : this.events.values()) {
            if (eventColors.get(event.getType().toUpperCase()) == null) {
                eventColors.put(event.getType().toUpperCase(), colors.get(colorIndex));
                if (colorIndex == colors.size() - 1) {
                    colorIndex = 0;
                } else {
                    colorIndex++;
                }
            }
        }
    }

    public void clear() {
        authToken = null;
        personID = null;
        people = null;
        events = null;
        personEvents = null;
        paternalAncestors = null;
        maternalAncestors = null;
    }
}
