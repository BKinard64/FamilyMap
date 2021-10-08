package service.requests;

public class FillRequest {
    private String username;
    private int generations;

    public FillRequest(String username, int generations) {
        this.username = username;
        this.generations = generations;
    }
}
