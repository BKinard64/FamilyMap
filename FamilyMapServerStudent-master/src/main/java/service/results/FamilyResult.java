package service.results;

import model.Person;

import java.util.List;

public class FamilyResult {
    private List<Person> data;
    private String message;
    private boolean success;

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
