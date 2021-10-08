package model;

import java.util.Objects;

/**
 * A record from the AuthToken table in the database.
 */
public class AuthToken {
    /**
     * The username associated with this token.
     */
    private String username;
    /**
     * The unique token generated for the given User.
     */
    private String token;

    /**
     * Create an AuthToken with the following parameters.
     *
     * @param username the username the token is being generated for.
     * @param token the unique token generated for the given User.
     */
    public AuthToken(String username, String token) {
        this.username = username;
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthToken authToken = (AuthToken) o;
        return username.equals(authToken.username) && token.equals(authToken.token);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, token);
    }

    @Override
    public String toString() {
        return "AuthToken{" +
                "username='" + username + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}
