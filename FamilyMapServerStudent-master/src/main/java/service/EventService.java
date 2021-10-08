package service;

import service.requests.EventRequest;
import service.results.EventResult;

/**
 * A service object for the event/[eventID] API.
 */
public class EventService {

    /**
     * Create an EventService object.
     */
    public EventService() {}

    /**
     * Returns the single Event object with the provided event ID.
     *
     * @param r an EventRequest object.
     * @return an EventResult object.
     */
    public EventResult event(EventRequest r) {return null;}
}
