package dao;

import model.AuthToken;

import java.sql.Connection;

/**
 * A data-access object for the AuthToken table.
 */
public class AuthTokenDao {
    /**
     * A connection to the database.
     */
    private final Connection conn;

    /**
     * Create an AuthTokenDao with a connection.
     *
     * @param conn a connection to the database.
     */
    public AuthTokenDao(Connection conn) { this.conn = conn; }

    /**
     * Insert an AuthToken into the AuthToken table.
     *
     * @param token the AuthToken to insert.
     */
    public void insert(AuthToken token) {}

    /**
     * Find an AuthToken with the given ID.
     *
     * @param tokenID the ID of the AuthToken to find.
     * @return an AuthToken object corresponding to the given ID.
     */
    public AuthToken find(String tokenID) {return null;}

    /**
     * Remove an AuthToken from the AuthToken table.
     * @param token the token to remove.
     */
    public void delete(AuthToken token) {}
}
