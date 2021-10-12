package service.requests;

/**
 * A request object for the event/[eventID] API.
 */
public class EventRequest {
    /**
     * The ID of the Event to find.
     */
    private String eventID;

    /**
     * Create an EventRequest object.
     *
     * @param eventID the ID of the Event to find.
     */
    public EventRequest(String eventID) {
        this.eventID = eventID;
    }

    public String getEventID() {
        return eventID;
    }
}
