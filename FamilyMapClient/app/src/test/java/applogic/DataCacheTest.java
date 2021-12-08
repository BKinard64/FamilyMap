package applogic;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

import model.Event;
import model.Person;
import requests.LoginRequest;
import results.FamilyEventsResult;
import results.FamilyResult;
import results.LoginResult;

public class DataCacheTest {
    private final ServerProxy serverProxy = new ServerProxy("localhost", "8080");

    @BeforeEach
    public void setUp() {
        DataCache.getInstance().clear();
        // Load pass-off data into database
        serverProxy.load();
        // Log sheila in
        LoginResult result = serverProxy.login(new LoginRequest("sheila", "parker"));
        DataCache.getInstance().setAuthToken(result.getAuthtoken());
        DataCache.getInstance().setPersonID(result.getPersonID());
        // Get family data
        FamilyResult famResult = serverProxy.getFamily();
        FamilyEventsResult famEventsResult = serverProxy.getFamilyEvents();
        // Store data in DataCache
        DataCache.getInstance().setPeople(famResult.getData());
        DataCache.getInstance().setEvents(famEventsResult.getData());
    }

    @AfterEach
    public void cleanUp() {
        serverProxy.load();
        DataCache.getInstance().clear();
    }

    @Test
    public void calculateFamilyRelationshipsFailure() {
        Map<String, List<Person>> familyMembers = DataCache.getInstance().getFamilyMembers();

        // Relationships have not been calculated so returning map should be null
        assertNull(familyMembers);
    }

    @Test
    public void calculateFamilyRelationshipsSuccess() {
        DataCache.getInstance().setFamilyMembers();
        Map<String, List<Person>> familyMembers = DataCache.getInstance().getFamilyMembers();

        // Every person should be in the map
        assertEquals(DataCache.getInstance().getPeople().size(), familyMembers.size());

        // Sheila Parker should have her spouse and 2 parents in her list
        List<Person> sheilaParkerFamily = familyMembers.get("Sheila_Parker");
        assertEquals(3, sheilaParkerFamily.size());
        assertTrue(sheilaParkerFamily.contains(DataCache.getInstance().getPeople().get("Davis_Hyer")));
        assertTrue(sheilaParkerFamily.contains(DataCache.getInstance().getPeople().get("Blaine_McGary")));
        assertTrue(sheilaParkerFamily.contains(DataCache.getInstance().getPeople().get("Betty_White")));

        // Blaine McGary should have his spouse, 2 parents, and daughter in his list
        List<Person> blaineMcgaryFamily = familyMembers.get("Blaine_McGary");
        assertEquals(4, blaineMcgaryFamily.size());
        assertTrue(blaineMcgaryFamily.contains(DataCache.getInstance().getPeople().get("Betty_White")));
        assertTrue(blaineMcgaryFamily.contains(DataCache.getInstance().getPeople().get("Ken_Rodham")));
        assertTrue(blaineMcgaryFamily.contains(DataCache.getInstance().getPeople().get("Mrs_Rodham")));
        assertTrue(blaineMcgaryFamily.contains(DataCache.getInstance().getPeople().get("Sheila_Parker")));
    }

    @Test
    public void eventsUnfilteredSuccess() {
        DataCache.getInstance().setGenderGroups();
        DataCache.getInstance().setPaternalAncestors();
        DataCache.getInstance().setMaternalAncestors();
        DataCache.getInstance().setPersonEventPool();

        Set<String> eventPersonIDs = DataCache.getInstance().getPersonEventPool();

        // Because nothing is filtered, eventPersonIDs should contain every person in Database
        assertEquals(DataCache.getInstance().getPeople().size(), eventPersonIDs.size());
    }

    @Test
    public void eventsFilteredSuccess() {
        DataCache.getInstance().setGenderGroups();
        DataCache.getInstance().setPaternalAncestors();
        DataCache.getInstance().setMaternalAncestors();

        Set<String> eventPersonIDs;

        // Filter out male events
        DataCache.getInstance().setMaleEventsVisible(false);
        DataCache.getInstance().setPersonEventPool();
        eventPersonIDs = DataCache.getInstance().getPersonEventPool();

        // There are 4 females, so eventPersonIDs should be of those 4 females
        assertEquals(4, eventPersonIDs.size());
        assertTrue(eventPersonIDs.contains("Sheila_Parker"));
        assertTrue(eventPersonIDs.contains("Betty_White"));
        assertTrue(eventPersonIDs.contains("Mrs_Rodham"));
        assertTrue(eventPersonIDs.contains("Mrs_Jones"));

        // Filter out female events
        DataCache.getInstance().setMaleEventsVisible(true);
        DataCache.getInstance().setFemaleEventsVisible(false);
        DataCache.getInstance().setPersonEventPool();
        eventPersonIDs = DataCache.getInstance().getPersonEventPool();

        // There are 4 males, so eventPersonIDs should be of those 4 males
        assertEquals(4, eventPersonIDs.size());
        assertTrue(eventPersonIDs.contains("Davis_Hyer"));
        assertTrue(eventPersonIDs.contains("Blaine_McGary"));
        assertTrue(eventPersonIDs.contains("Ken_Rodham"));
        assertTrue(eventPersonIDs.contains("Frank_Jones"));

        // Filter out male AND female events
        DataCache.getInstance().setMaleEventsVisible(false);
        DataCache.getInstance().setPersonEventPool();
        eventPersonIDs = DataCache.getInstance().getPersonEventPool();

        // If neither male or female events are meant to be displayed, returning set should be empty
        assertTrue(eventPersonIDs.isEmpty());

        // Filter out paternal ancestors
        DataCache.getInstance().setMaleEventsVisible(true);
        DataCache.getInstance().setFemaleEventsVisible(true);
        DataCache.getInstance().setFatherSideVisible(false);
        DataCache.getInstance().setPersonEventPool();
        eventPersonIDs = DataCache.getInstance().getPersonEventPool();

        // There are 5 people through Sheila's mother's side (including Sheila and Davis)
        assertEquals(5, eventPersonIDs.size());
        assertTrue(eventPersonIDs.contains("Sheila_Parker"));
        assertTrue(eventPersonIDs.contains("Davis_Hyer"));
        assertTrue(eventPersonIDs.contains("Betty_White"));
        assertTrue(eventPersonIDs.contains("Mrs_Jones"));
        assertTrue(eventPersonIDs.contains("Frank_Jones"));

        // Filter out maternal ancestors
        DataCache.getInstance().setFatherSideVisible(true);
        DataCache.getInstance().setMotherSideVisible(false);
        DataCache.getInstance().setPersonEventPool();
        eventPersonIDs = DataCache.getInstance().getPersonEventPool();

        // There are 5 people through Sheila's mother's side (including Sheila and Davis)
        assertEquals(5, eventPersonIDs.size());
        assertTrue(eventPersonIDs.contains("Sheila_Parker"));
        assertTrue(eventPersonIDs.contains("Davis_Hyer"));
        assertTrue(eventPersonIDs.contains("Blaine_McGary"));
        assertTrue(eventPersonIDs.contains("Ken_Rodham"));
        assertTrue(eventPersonIDs.contains("Mrs_Rodham"));

        // Filter out maternal AND paternal ancestors
        DataCache.getInstance().setFatherSideVisible(false);
        DataCache.getInstance().setPersonEventPool();
        eventPersonIDs = DataCache.getInstance().getPersonEventPool();

        // Person event pool should just include sheila and davis (user and spouse)
        assertEquals(2, eventPersonIDs.size());
        assertTrue(eventPersonIDs.contains("Sheila_Parker"));
        assertTrue(eventPersonIDs.contains("Davis_Hyer"));
    }

    @Test
    public void orderPersonEventsWithBirth() {
        DataCache.getInstance().setPersonEvents();

        Map<String, PriorityQueue<Event>> personEvents = DataCache.getInstance().getPersonEvents();
        PriorityQueue<Event> sheilaEvents = personEvents.get("Sheila_Parker");

        // Sheila should have 5 events
        assertEquals(5, sheilaEvents.size());

        // The first event should be birth
        Event event = sheilaEvents.poll();
        assertEquals("BIRTH", event.getType().toUpperCase());

        // The next event should be marriage
        event = sheilaEvents.poll();
        assertEquals("MARRIAGE", event.getType().toUpperCase());

        // The next two events should be completed asteroids
        event = sheilaEvents.poll();
        assertEquals("COMPLETED ASTEROIDS", event.getType().toUpperCase());
        event = sheilaEvents.poll();
        assertEquals("COMPLETED ASTEROIDS", event.getType().toUpperCase());

        // The last event should be death
        event = sheilaEvents.poll();
        assertEquals("DEATH", event.getType().toUpperCase());
    }

    @Test
    public void orderPersonEventsNoBirth() {
        DataCache.getInstance().setPersonEvents();

        Map<String, PriorityQueue<Event>> personEvents = DataCache.getInstance().getPersonEvents();
        PriorityQueue<Event> kenEvents = personEvents.get("Ken_Rodham");

        // Ken should have 2 events
        assertEquals(2, kenEvents.size());

        // The first event should be graduated from BYU
        Event event = kenEvents.poll();
        assertEquals("GRADUATED FROM BYU", event.getType().toUpperCase());

        // The last event should be marriage
        event = kenEvents.poll();
        assertEquals("MARRIAGE", event.getType().toUpperCase());
    }

    @Test
    public void searchResultsSuccess() {
        DataCache.getInstance().organizeData();
        List<Event> searchResultEvents = DataCache.getInstance().findEventsMatchingSearch("fr");
        List<Person> searchResultsPeople = DataCache.getInstance().findPeopleMatchingSearch("fr");

        // Events that should be returned by this search are Caught a Frog and Graduated From BYU
        assertEquals(2, searchResultEvents.size());
        assertEquals("Jones_Frog", searchResultEvents.get(0).getId());
        assertEquals("BYU_graduation", searchResultEvents.get(1).getId());

        // People that should be returned by this search is only Frank Jones
        assertEquals(1, searchResultsPeople.size());
        assertEquals("Frank_Jones", searchResultsPeople.get(0).getId());
    }

    @Test
    public void noSearchResultsSuccess() {
        DataCache.getInstance().organizeData();
        List<Event> searchResultEvents = DataCache.getInstance().findEventsMatchingSearch("zy");
        List<Person> searchResultsPeople = DataCache.getInstance().findPeopleMatchingSearch("zy");

        // There should be no Events or People returned from this search
        assertTrue(searchResultEvents.isEmpty());
        assertTrue(searchResultsPeople.isEmpty());
    }
}
