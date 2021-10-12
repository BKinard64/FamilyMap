package service.results;

/**
 * A result object for the event/[eventID] API.
 */
public class EventResult {
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
     * An error message in the event of a failed request.
     */
    private String message;
    /**
     * The latitude of the Event's location.
     */
    private float latitude;
    /**
     * The longitude of the Event's location.
     */
    private float longitude;
    /**
     * The year the Event occurred.
     */
    private int year;
    /**
     * An indicator of a successful/failed request.
     */
    private boolean success;

    /**
     * Create an EventResult object.
     *
     * @param associatedUsername the username associated with the current User.
     * @param eventID the ID of the event to find.
     * @param personID the ID of the Person associated with the current User.
     * @param country the country where the Event occurred.
     * @param city the city where the Event occurred.
     * @param eventType the type of the Event.
     * @param message an error message in the event the request fails.
     * @param latitude the latitude of the Event's location.
     * @param longitude the longitude of the Event's location.
     * @param year the year the Event occurred.
     * @param success an indicator of a successful/failed request.
     */
    public EventResult(String associatedUsername, String eventID, String personID, String country, String city,
                       String eventType, String message, float latitude, float longitude, int year, boolean success) {
        this.associatedUsername = associatedUsername;
        this.eventID = eventID;
        this.personID = personID;
        this.country = country;
        this.city = city;
        this.eventType = eventType;
        this.message = message;
        this.latitude = latitude;
        this.longitude = longitude;
        this.year = year;
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

    public String getMessage() {
        return message;
    }

    public float getLatitude() {
        return latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public int getYear() {
        return year;
    }

    public boolean isSuccess() {
        return success;
    }
}
