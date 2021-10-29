package dao;

import model.AuthToken;

import java.sql.*;

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
     * @throws DataAccessException thrown if SQLException occurs
     */
    public void insert(AuthToken token) throws DataAccessException {
        // Create string to pass into the connection's prepareStatement method
        String sql = "INSERT INTO auth_token (token, username) VALUES(?,?)";
        // Create a PreparedStatement object using the string above
        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            // Pass the members of the provided User object to the Statement object
            stmt.setString(1, token.getToken());
            stmt.setString(2, token.getUsername());

            // Execute the statement with the passed-in members
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while inserting into the database");
        }
    }

    /**
     * Find an AuthToken with the given ID.
     *
     * @param tokenID the ID of the AuthToken to find.
     * @throws DataAccessException thrown if SQLException occurs
     * @return an AuthToken object corresponding to the given ID.
     */
    public AuthToken find(String tokenID) throws DataAccessException {
        AuthToken token;
        ResultSet rs = null;
        String sql = "SELECT * FROM auth_token WHERE token = ?;";
        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, tokenID);
            rs = stmt.executeQuery();
            if (rs.next()) {
                token = new AuthToken(rs.getString("token"), rs.getString("username"));
                return token;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding AuthToken");
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * Remove an AuthToken from the AuthToken table.
     *
     * @param tokenID the ID of the AuthToken to remove.
     * @throws DataAccessException thrown if SQLException occurs
     */
    public void delete(String tokenID) throws DataAccessException {
        String sql = "DELETE FROM auth_token WHERE token = ?;";
        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, tokenID);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while deleting AuthToken");
        }
    }

    /**
     * Remove all records from the AuthToken table.
     *
     * @throws DataAccessException thrown if SQLException occurs
     */
    public void clear() throws DataAccessException {
        try (Statement stmt = conn.createStatement()){
            String sql = "DELETE FROM auth_token";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while clearing auth_token table");
        }
    }
}
