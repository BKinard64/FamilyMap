package model;

import java.util.Objects;

/**
 * A record from the Event table in the database.
 */
public class Event {
    /**
     * The unique identifier for this event.
     */
    private String id;
    /**
     * The username to which this person belongs.
     */
    private String username;
    /**
     * The ID of the person to which this event belongs.
     */
    private String personID;
    /**
     * The latitude of the event's location.
     */
    private float latitude;
    /**
     * The longitude of the event's location.
     */
    private float longitude;
    /**
     * The country in which the event occurred.
     */
    private String country;
    /**
     * The city in which the event occurred.
     */
    private String city;
    /**
     * The type of the event.
     */
    private String type;
    /**
     * The year in which the event occurred.
     */
    private int year;

    /**
     * Creates an event with the following parameters.
     *
     * @param id the event's unique identifier.
     * @param username the person's associated username to which this event belongs.
     * @param personID the unique identifier of the person to which this event belongs.
     * @param latitude the latitude of the event's location.
     * @param longitude the longitude of the event's location.
     * @param country the country in which the event occurred.
     * @param city the city in which the event occurred.
     * @param type the type of the event.
     * @param year the year in which the event occurred.
     */
    public Event(String id, String username, String personID, float latitude, float longitude, String country,
                 String city, String type, int year) {
        this.id = id;
        this.username = username;
        this.personID = personID;
        this.latitude = latitude;
        this.longitude = longitude;
        this.country = country;
        this.city = city;
        this.type = type;
        this.year = year;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
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
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
                year == event.year && id.equals(event.id) && Objects.equals(username, event.username) &&
                Objects.equals(personID, event.personID) && Objects.equals(country, event.country) &&
                Objects.equals(city, event.city) && Objects.equals(type, event.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, personID, latitude, longitude, country, city, type, year);
    }

    @Override
    public String toString() {
        return "Event{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", personID='" + personID + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", country='" + country + '\'' +
                ", city='" + city + '\'' +
                ", type='" + type + '\'' +
                ", year=" + year +
                '}';
    }
}
