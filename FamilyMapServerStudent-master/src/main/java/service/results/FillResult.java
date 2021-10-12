package service.results;

/**
 * A result object for the fill API.
 */
public class FillResult {
    /**
     * A message describing the success/failure of the request.
     */
    private String message;
    /**
     * An indicator of a successful/unsuccessful fill.
     */
    private String success;

    /**
     * Create a FillResult object.
     *
     * @param message a message describing the success/failure of the request.
     * @param success an indicator of a successful/unsuccessful fill.
     */
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
