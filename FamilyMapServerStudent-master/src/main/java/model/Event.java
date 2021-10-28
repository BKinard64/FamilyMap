package model;

import java.util.Objects;

/**
 * A record from the Event table in the database.
 */
public class Event {
    /**
     * The unique identifier for this Event.
     */
    private String eventID;
    /**
     * The username to which this Event belongs.
     */
    private String associatedUsername;
    /**
     * The ID of the Person to which this Event belongs.
     */
    private String personID;
    /**
     * The latitude of the Event's location.
     */
    private float latitude;
    /**
     * The longitude of the Event's location.
     */
    private float longitude;
    /**
     * The country in which the Event occurred.
     */
    private String country;
    /**
     * The city in which the Event occurred.
     */
    private String city;
    /**
     * The type of the Event.
     */
    private String eventType;
    /**
     * The year in which the Event occurred.
     */
    private int year;

    public Event() {}

    /**
     * Creates an Event with the following parameters.
     *
     * @param eventID the Event's unique identifier.
     * @param associatedUsername the Person's associated username to which this Event belongs.
     * @param personID the unique identifier of the Person to which this Event belongs.
     * @param latitude the latitude of the Event's location.
     * @param longitude the longitude of the Event's location.
     * @param country the country in which the Event occurred.
     * @param city the city in which the Event occurred.
     * @param eventType the type of the Event.
     * @param year the year in which the Event occurred.
     */
    public Event(String eventID, String associatedUsername, String personID, float latitude, float longitude, String country,
                 String city, String eventType, int year) {
        this.eventID = eventID;
        this.associatedUsername = associatedUsername;
        this.personID = personID;
        this.latitude = latitude;
        this.longitude = longitude;
        this.country = country;
        this.city = city;
        this.eventType = eventType;
        this.year = year;
    }

    public String getId() {
        return eventID;
    }

    public void setId(String eventID) {
        this.eventID = eventID;
    }

    public String getUsername() {
        return associatedUsername;
    }

    public void setUsername(String associatedUsername) {
        this.associatedUsername = associatedUsername;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getType() {
        return eventType;
    }

    public void setType(String eventType) {
        this.eventType = eventType;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Double.compare(event.latitude, latitude) == 0 && Double.compare(event.longitude, longitude) == 0 &&
                year == event.year && eventID.equals(event.eventID) &&
                Objects.equals(associatedUsername, event.associatedUsername) &&
                Objects.equals(personID, event.personID) && Objects.equals(country, event.country) &&
                Objects.equals(city, event.city) && Objects.equals(eventType, event.eventType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventID, associatedUsername, personID, latitude, longitude, country, city, eventType, year);
    }

    @Override
    public String toString() {
        return "Event{" +
                "id='" + eventID + '\'' +
                ", username='" + associatedUsername + '\'' +
                ", personID='" + personID + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", country='" + country + '\'' +
                ", city='" + city + '\'' +
                ", type='" + eventType + '\'' +
                ", year=" + year +
                '}';
    }
}
