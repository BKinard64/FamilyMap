package service.results;

public class EventResult {
    private String associatedUsername;
    private String eventID;
    private String personID;
    private String country;
    private String city;
    private String eventType;
    private String message;
    private float latitude;
    private float longitude;
    private int year;
    private boolean success;

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
}
