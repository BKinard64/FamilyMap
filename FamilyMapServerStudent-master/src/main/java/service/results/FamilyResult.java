package service.results;

import model.Person;

import java.util.List;

/**
 * A result object for the person API.
 */
public class FamilyResult {
    /**
     * A list of family members of the current User.
     */
    private List<Person> data;
    /**
     * An error message in the event the request fails.
     */
    private String message;
    /**
     * An indicator of a successful/unsuccessful request.
     */
    private boolean success;

    /**
     * Create a FamilyResult object.
     *
     * @param data a list of family members of the current User.
     * @param message an error message in the event the request fails.
     * @param success an indicator of a successful/unsuccessful request.
     */
    public FamilyResult(List<Person> data, String message, boolean success) {
        this.data = data;
        this.message = message;
        this.success = success;
    }

    public List<Person> getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return success;
    }
}
