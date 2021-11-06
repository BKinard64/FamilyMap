package results;

/**
 * A result object for the fill API.
 */
public class FillResult extends Result {
    /**
     * Create a FillResult object.
     *
     * @param message a message describing the success/failure of the request.
     * @param success an indicator of a successful/unsuccessful fill.
     */
    public FillResult(String message, boolean success) {
        this.message = message;
        this.success = success;
    }
}
