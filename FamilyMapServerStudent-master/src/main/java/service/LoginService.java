package service;

import dao.DataAccessException;
import dao.Database;
import model.User;
import service.requests.LoginRequest;
import service.results.LoginResult;

/**
 * A service object for the login API.
 */
public class LoginService extends Service {

    /**
     * Create a LoginService object.
     */
    public LoginService() {
        db = new Database();
    }

    /**
     * Log a User into the database.
     *
     * @param r a LoginRequest object.
     * @return a LoginResult object.
     */
    public LoginResult login(LoginRequest r) {
        try {
            db.openConnection();

            String username = r.getUsername();
            User user = validateUser(username);

            // Confirm username is registered
            if (user != null) {

                // Confirm password is valid
                if (r.getPassword().equals(user.getPassword())) {

                    String tokenString = generateAuthToken(username);
                    db.closeConnection(true);
                    return new LoginResult(tokenString, username, user.getPersonID(), null, true);

                } else {
                    db.closeConnection(false);
                    return new LoginResult("Error: Invalid password.", false);
                }
            } else {
                db.closeConnection(false);
                return new LoginResult("Error: Username is not registered.", false);
            }
        } catch (DataAccessException ex) {
            ex.printStackTrace();
            try {
                db.closeConnection(false);
            } catch (DataAccessException e) {
                e.printStackTrace();
            }
            return new LoginResult("Error: Internal server error.", false);
        }
    }
}
