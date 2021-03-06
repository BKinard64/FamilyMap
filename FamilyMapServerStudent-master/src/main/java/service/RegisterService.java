package service;

import dao.DataAccessException;
import dao.Database;
import dao.UserDao;
import jsondata.FemaleNames;
import jsondata.LocationData;
import jsondata.MaleNames;
import jsondata.Surnames;
import model.AncestryGenerator;
import model.User;
import requests.RegisterRequest;
import results.RegisterResult;

/**
 * A service object for the register API.
 */
public class RegisterService extends Service {
    private final LocationData locData;
    private final FemaleNames fmlNames;
    private final MaleNames mlNames;
    private final Surnames srNames;

    /**
     * Create a RegisterService object.
     */
    public RegisterService(LocationData locData, FemaleNames fmlNames, MaleNames mlNames, Surnames srNames) {
        this.db = new Database();
        this.locData = locData;
        this.fmlNames = fmlNames;
        this.mlNames = mlNames;
        this.srNames = srNames;
    }

    /**
     * Register a User in the database.
     *
     * @param r a RegisterRequest object.
     * @return a RegisterResult object.
     */
    public RegisterResult register(RegisterRequest r) {
        try {
            db.openConnection();

            // Confirm properties are not missing
            if (r.getUsername() != null && r.getPassword() != null && r.getEmail() != null && r.getFirstName() != null
                && r.getLastName() != null && r.getGender() != null) {

                // Confirm username is not already taken
                UserDao uDao = new UserDao(db.getConnection());
                User existingUser = uDao.find(r.getUsername());
                if (existingUser == null) {

                    // Create new User
                    User user = new User(r.getUsername(), r.getPassword(), r.getEmail(), r.getFirstName(), r.getLastName(),
                            r.getGender(), "null");
                    // Add User to Database
                    uDao.insert(user);

                    // Generate 4 Generations of Data for new User
                    AncestryGenerator generator = new AncestryGenerator(db.getConnection(), user, 4,
                            locData, fmlNames, mlNames, srNames);
                    generator.generateFamilyData();

                    // Find User in database
                    String username = r.getUsername();
                    user = uDao.find(username);

                    String tokenString = generateAuthToken(username);

                    db.closeConnection(true);

                    return new RegisterResult(tokenString, username, user.getPersonID(), true);

                } else {
                    // Username already taken by another user
                    db.closeConnection(false);
                    return new RegisterResult("Error: Username already taken by another user.", false);
                }
            } else {
                // Missing necessary property
                db.closeConnection(false);
                return new RegisterResult("Error: Request property missing.", false);
            }
        } catch (DataAccessException ex) {
            ex.printStackTrace();
            try {
                db.closeConnection(false);
            } catch (DataAccessException e) {
                e.printStackTrace();
            }
            return new RegisterResult("Error: Internal server error.", false);
        }
    }
}
