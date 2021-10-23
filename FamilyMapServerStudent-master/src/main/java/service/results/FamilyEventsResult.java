package service.results;

import model.Event;

import java.util.List;

/**
 * A result object for the event API.
 */
public class FamilyEventsResult extends Result {
    /**
     * A list of every Event for every family member of the current User.
     */
    private List<Event> data;

    /**
     * Create a FamilyEventsResult.
     *
     * @param data a list of every Event for every family member of the current User.
     * @param message an error message in the event the request fails.
     * @param success an indicator of a successful/unsuccessful request.
     */
    public FamilyEventsResult(List<Event> data, String message, boolean success) {
        this.data = data;
        this.message = message;
        this.success = success;
    }

    public List<Event> getData() {
        return data;
    }
}
