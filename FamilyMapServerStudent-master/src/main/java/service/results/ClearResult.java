package service.results;

public class ClearResult {
    private String message;
    private boolean success;

    public ClearResult(String message, boolean success) {
        this.message = message;
        this.success = success;
    }
}
