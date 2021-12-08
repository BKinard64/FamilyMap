package applogic;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Polyline;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
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
    private boolean lifeStoryLinesEnabled = true;
    private boolean familyTreeLinesEnabled = true;
    private boolean spouseLinesEnabled = true;
    private boolean fatherSideVisible = true;
    private boolean motherSideVisible = true;
    private boolean maleEventsVisible = true;
    private boolean femaleEventsVisible = true;
    private boolean filterStatusChanged = false;
    private Map<String, Person> people;
    private Map<String, Event> events;
    private Map<String, PriorityQueue<Event>> personEvents;
    private Map<String, List<Person>> familyMembers;
    private Map<String, Float> eventColors;
    private Set<String> paternalAncestors;
    private Set<String> maternalAncestors;
    private Set<String> malePeople;
    private Set<String> femalePeople;

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

    public boolean isLifeStoryLinesEnabled() {
        return lifeStoryLinesEnabled;
    }

    public void setLifeStoryLinesEnabled(boolean lifeStoryLinesEnabled) {
        this.lifeStoryLinesEnabled = lifeStoryLinesEnabled;
    }

    public boolean isFamilyTreeLinesEnabled() {
        return familyTreeLinesEnabled;
    }

    public void setFamilyTreeLinesEnabled(boolean familyTreeLinesEnabled) {
        this.familyTreeLinesEnabled = familyTreeLinesEnabled;
    }

    public boolean isSpouseLinesEnabled() {
        return spouseLinesEnabled;
    }

    public void setSpouseLinesEnabled(boolean spouseLinesEnabled) {
        this.spouseLinesEnabled = spouseLinesEnabled;
    }

    public boolean isFatherSideVisible() {
        return fatherSideVisible;
    }

    public void setFatherSideVisible(boolean fatherSideVisible) {
        this.fatherSideVisible = fatherSideVisible;
    }

    public boolean isMotherSideVisible() {
        return motherSideVisible;
    }

    public void setMotherSideVisible(boolean motherSideVisible) {
        this.motherSideVisible = motherSideVisible;
    }

    public boolean isMaleEventsVisible() {
        return maleEventsVisible;
    }

    public void setMaleEventsVisible(boolean maleEventsVisible) {
        this.maleEventsVisible = maleEventsVisible;
    }

    public boolean isFemaleEventsVisible() {
        return femaleEventsVisible;
    }

    public void setFemaleEventsVisible(boolean femaleEventsVisible) {
        this.femaleEventsVisible = femaleEventsVisible;
    }

    public boolean isFilterStatusChanged() {
        return filterStatusChanged;
    }

    public void setFilterStatusChanged(boolean filterStatusChanged) {
        this.filterStatusChanged = filterStatusChanged;
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

    public void organizeData() {
        setPersonEvents();
        setFamilyMembers();
        setPaternalAncestors();
        setMaternalAncestors();
        setGenderGroups();
        setEventColors();
    }

    public Map<String, PriorityQueue<Event>> getPersonEvents() {
        return personEvents;
    }

    public void setPersonEvents() {
        personEvents = new HashMap<>();
        for (Event event : this.events.values()) {
            if (personEvents.get(event.getPersonID()) == null) {
                PriorityQueue<Event> initEventList = new PriorityQueue<>(3,
                        new Comparator<Event>() {
                            @Override
                            public int compare(Event o1, Event o2) {
                                if (o1.getYear() == o2.getYear()) {
                                    return o1.getType().toLowerCase().compareTo(o2.getType().toLowerCase());
                                }
                                return o1.getYear() - o2.getYear();
                            }
                        });
                initEventList.add(event);
                personEvents.put(event.getPersonID(), initEventList);
            } else {
                personEvents.get(event.getPersonID()).add(event);
            }
        }
    }

    public Map<String, List<Person>> getFamilyMembers() {
        return familyMembers;
    }

    public void setFamilyMembers() {
        familyMembers = new HashMap<>();
        for (String personID : this.people.keySet()) {
            List<Person> family = new ArrayList<>();
            // Add the father, mother, and spouse
            Person person = this.people.get(personID);
            if (person.getFatherID() != null) {
                family.add(this.people.get(person.getFatherID()));
            }
            if (person.getMotherID() != null) {
                family.add(this.people.get(person.getMotherID()));
            }
            if (person.getSpouseID() != null) {
                family.add(this.people.get(person.getSpouseID()));
            }

            // Add the children
            for (Person child : this.people.values()) {
                if (personID.equals(child.getFatherID()) || personID.equals(child.getMotherID())) {
                    family.add(child);
                }
            }

            // Store family members in map
            familyMembers.put(personID, family);
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

    public Set<String> getPaternalAncestors() {
        return paternalAncestors;
    }

    public void setPaternalAncestors() {
        Person user = getPerson();
        paternalAncestors = new HashSet<>();

        String fatherID = user.getFatherID();
        if (fatherID != null) {
            // Add user's fatherID to this Set
            paternalAncestors.add(fatherID);

            // Get the father's ancestors and add them to the Set
            Person father = this.people.get(fatherID);
            if (father != null) {
                getAncestors(father, paternalAncestors);
            }
        }
    }

    public Set<String> getMaternalAncestors() {
        return maternalAncestors;
    }

    public void setMaternalAncestors() {
        Person user = getPerson();
        maternalAncestors = new HashSet<>();

        String motherID = user.getMotherID();
        if (motherID != null) {
            // Add user's mother to this Set
            maternalAncestors.add(motherID);

            // Get the mother's ancestors and add them to the Set
            Person mother = this.people.get(motherID);
            if (mother != null) {
                getAncestors(mother, maternalAncestors);
            }
        }
    }

    private void getAncestors(Person person, Set<String> ancestors) {
        // Add father to Set
        String fatherID = person.getFatherID();
        if (fatherID != null) {
            ancestors.add(fatherID);
            Person father = this.people.get(person.getFatherID());
            if (father != null) {
                getAncestors(father, ancestors);
            }
        }

        // Add mother to Set
        String motherID = person.getMotherID();
        if (motherID != null) {
            ancestors.add(motherID);
            Person mother = this.people.get(person.getMotherID());
            if (mother != null) {
                getAncestors(mother, ancestors);
            }
        }
    }

    public Set<String> getMalePeople() {
        return malePeople;
    }

    public void setGenderGroups() {
        malePeople = new HashSet<>();
        femalePeople = new HashSet<>();

        for (Person person : this.people.values()) {
            if (person.getGender().equals("m")) {
                malePeople.add(person.getId());
            } else {
                femalePeople.add(person.getId());
            }
        }
    }

    public Set<String> getFemalePeople() {
        return femalePeople;
    }

    public void clear() {
        authToken = null;
        personID = null;
        people = null;
        events = null;
        eventColors = null;
        personEvents = null;
        paternalAncestors = null;
        maternalAncestors = null;
    }
}
