package service.results;

/**
 * A result object for the Clear API.
 */
public class ClearResult {
    /**
     * A message describing the success/failure of the request.
     */
    private String message;
    /**
     * An indicator of a successful/failed request.
     */
    private boolean success;

    /**
     * Create a ClearResult object.
     *
     * @param message a message desribing the success/failure of the request.
     * @param success an indicator of a success/failed request.
     */
    public ClearResult(String message, boolean success) {
        this.message = message;
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return success;
    }
}
