package requests;

/**
 * A request object for the event/[eventID] API.
 */
public class EventRequest {
    /**
     * The ID of the Event to find.
     */
    private String eventID;

    /**
     * The authToken sent with the request.
     */
    private String authToken;

    /**
     * Create an EventRequest object.
     *
     * @param eventID the ID of the Event to find.
     * @param authToken the authToken sent with the request.
     */
    public EventRequest(String eventID, String authToken) {
        this.eventID = eventID;
        this.authToken = authToken;
    }

    public String getEventID() {
        return eventID;
    }

    public String getAuthToken() { return authToken; }
}
