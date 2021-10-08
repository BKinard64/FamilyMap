package service.results;

public class FillResult {
    private String message;
    private String success;

    public FillResult(String message, String success) {
        this.message = message;
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public String getSuccess() {
        return success;
    }
}
