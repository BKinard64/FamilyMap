package service.results;

/**
 * A parent Result class
 */
public class Result {
    /**
     * A message describing the success/failure of the request.
     */
    protected String message;
    /**
     * An indicator of the request being successful or not.
     */
    protected boolean success;

    public Result(String messsage, boolean success) {
        this.message = messsage;
        this.success = success;
    }

    public Result() {}

    public String getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return success;
    }
}
