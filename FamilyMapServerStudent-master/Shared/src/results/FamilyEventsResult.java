package results;

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
     * @param success an indicator of a successful/unsuccessful request.
     */
    public FamilyEventsResult(List<Event> data, boolean success) {
        this.data = data;
        this.success = success;
    }

    public FamilyEventsResult(String message, boolean success) {
        this.message = message;
        this.success = success;
    }

    public List<Event> getData() {
        return data;
    }
}
