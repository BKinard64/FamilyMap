package service.results;

/**
 * A result object for the load API.
 */
public class LoadResult extends Result {
    /**
     * Create a LoadResult object.
     *
     * @param message a message describing the success/failure of the request.
     * @param success an indicator of a successful/unsuccessful load.
     */
    public LoadResult(String message, boolean success) {
        this.message = message;
        this.success = success;
    }
}
