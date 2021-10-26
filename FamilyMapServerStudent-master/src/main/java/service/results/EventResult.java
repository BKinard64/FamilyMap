package service.results;

/**
 * A result object for the event/[eventID] API.
 */
public class EventResult extends Result {
    /**
     * The username associated with the current User.
     */
    private String associatedUsername;
    /**
     * The ID of the event to find.
     */
    private String eventID;
    /**
     * The ID of the Person associated with the current User.
     */
    private String personID;
    /**
     * The country where the Event occurred.
     */
    private String country;
    /**
     * The city where the Event occurred.
     */
    private String city;
    /**
     * The type of the Event.
     */
    private String eventType;
    /**
     * The latitude of the Event's location.
     */
    private double latitude;
    /**
     * The longitude of the Event's location.
     */
    private double longitude;
    /**
     * The year the Event occurred.
     */
    private int year;

    /**
     * Create an EventResult object.
     *
     * @param associatedUsername the username associated with the current User.
     * @param eventID the ID of the event to find.
     * @param personID the ID of the Person associated with the current User.
     * @param country the country where the Event occurred.
     * @param city the city where the Event occurred.
     * @param eventType the type of the Event.
     * @param latitude the latitude of the Event's location.
     * @param longitude the longitude of the Event's location.
     * @param year the year the Event occurred.
     * @param success an indicator of a successful/failed request.
     */
    public EventResult(String associatedUsername, String eventID, String personID, String country, String city,
                       String eventType, double latitude, double longitude, int year, boolean success) {
        this.associatedUsername = associatedUsername;
        this.eventID = eventID;
        this.personID = personID;
        this.country = country;
        this.city = city;
        this.eventType = eventType;
        this.latitude = latitude;
        this.longitude = longitude;
        this.year = year;
        this.success = success;
    }

    public EventResult(String message, boolean success) {
        this.message = message;
        this.success = success;
    }

    public String getAssociatedUsername() {
        return associatedUsername;
    }

    public String getEventID() {
        return eventID;
    }

    public String getPersonID() {
        return personID;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    public String getEventType() {
        return eventType;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public int getYear() {
        return year;
    }
}
