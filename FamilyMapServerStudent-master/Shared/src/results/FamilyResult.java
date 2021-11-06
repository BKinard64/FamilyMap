package results;

import model.Person;

import java.util.List;

/**
 * A result object for the person API.
 */
public class FamilyResult extends Result {
    /**
     * A list of family members of the current User.
     */
    private List<Person> data;

    /**
     * Create a FamilyResult object.
     *
     * @param data a list of family members of the current User.
     * @param success an indicator of a successful/unsuccessful request.
     */
    public FamilyResult(List<Person> data, boolean success) {
        this.data = data;
        this.success = success;
    }

    public FamilyResult(String message, boolean success) {
        this.message = message;
        this.success = success;
    }

    public List<Person> getData() {
        return data;
    }
}
