package service.requests;

/**
 * A request object for the fill API.
 */
public class FillRequest {
    /**
     * The username of the User to generate family data for.
     */
    private String username;
    /**
     * The number of generations of family data that should be generated.
     */
    private String generations;

    /**
     * Create a FillRequest object.
     *
     * @param username the username of the User to generate family data for.
     * @param generations the number of generations of family data to generate.
     */
    public FillRequest(String username, String generations) {
        this.username = username;
        this.generations = generations;
    }

    public String getUsername() {
        return username;
    }

    public String getGenerations() {
        return generations;
    }
}
