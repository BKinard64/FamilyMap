package results;

/**
 * A result object for the Clear API.
 */
public class ClearResult extends Result {
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
}
