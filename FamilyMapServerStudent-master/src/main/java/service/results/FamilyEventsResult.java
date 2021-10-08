package service.results;

import model.Event;

import java.util.List;

public class FamilyEventsResult {
    private List<Event> data;
    private String message;
    private boolean success;

    public FamilyEventsResult(List<Event> data, String message, boolean success) {
        this.data = data;
        this.message = message;
        this.success = success;
    }

    public List<Event> getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return success;
    }
}
