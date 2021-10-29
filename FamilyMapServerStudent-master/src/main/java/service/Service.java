package service;

import dao.AuthTokenDao;
import dao.DataAccessException;
import dao.Database;
import dao.UserDao;
import model.AuthToken;
import model.User;

import java.util.UUID;

/**
 * A parent Service class
 */
public class Service {
    /**
     * A Database object to connect to the database
     */
    protected Database db;

    /**
     * Create a Service object and instantiate the database
     */
    public Service() {
        db = new Database();
    }

    /**
     * Generate an AuthToken for a given User and store it in the database
     *
     * @param username the username of the User to create an AuthToken for
     * @return the authtoken String that is generated
     * @throws DataAccessException thrown if SQLException occurs
     */
    protected String generateAuthToken(String username) throws DataAccessException {
        String tokenString = UUID.randomUUID().toString();
        AuthToken authToken = new AuthToken(tokenString, username);
        new AuthTokenDao(db.getConnection()).insert(authToken);
        return tokenString;
    }

    /**
     * Confirm a provided AuthToken is actually in database
     *
     * @param tokenString the token to validate
     * @return an AuthToken object if provided AuthToken is valid, null otherwise
     * @throws DataAccessException thrown if SQLException occurs
     */
    protected AuthToken validateAuthToken(String tokenString) throws DataAccessException {
        AuthTokenDao atDao = new AuthTokenDao(db.getConnection());
        return atDao.find(tokenString);
    }

    /**
     * Confirm a provided username matches a User in the database
     *
     * @param username the username to validate
     * @return a User object if the username is valid, null otherwise
     * @throws DataAccessException thrown if SQLException occurs
     */
    protected User validateUser(String username) throws DataAccessException {
        UserDao uDao = new UserDao(db.getConnection());
        return uDao.find(username);
    }
}
