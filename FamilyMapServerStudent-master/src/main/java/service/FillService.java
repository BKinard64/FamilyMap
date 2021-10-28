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
import service.requests.FillRequest;
import service.results.FillResult;

/**
 * A service object for the fill API.
 */
public class FillService {
    private LocationData locData;
    private FemaleNames fmlNames;
    private MaleNames mlNames;
    private Surnames srNames;

    /**
     * Create a FillService object.
     */
    public FillService(LocationData locData, FemaleNames fmlNames, MaleNames mlNames, Surnames srNames) {
        this.locData = locData;
        this.fmlNames = fmlNames;
        this.mlNames = mlNames;
        this.srNames = srNames;
    }

    /**
     * Populate the database with generated data for specified User.
     *
     * @param r a FillRequest object.
     * @return a FillResult object.
     */
    public FillResult fill(FillRequest r) {
        Database db = new Database();
        try {
            db.openConnection();

            // Get username from Request object and validate username
            String username = r.getUsername();
            UserDao uDao = new UserDao(db.getConnection());
            User user = uDao.find(username);

            if (user != null) {

                // Confirm generations parameter is valid
                int generations = Integer.parseInt(r.getGenerations());
                if (generations >= 0) {

                    // Delete any existing data for current User and fill with new data
                    AncestryGenerator generator = new AncestryGenerator(db.getConnection(), user, generations,
                                                                        locData, fmlNames, mlNames, srNames);
                    generator.deleteFamilyData();
                    int[] generatedData = generator.generateFamilyData();
                    db.closeConnection(true);
                    return new FillResult("Successfully added " + generatedData[0] + " persons and " +
                                            generatedData[1] + " events to the database.", true);
                } else {
                    // Generations parameter is negative and therefore invalid
                    throw new NumberFormatException("Generations parameter must be non-negative");
                }
            } else {
                // The username is not valid
                db.closeConnection(false);
                return new FillResult("Error: Invalid username.", false);
            }
        } catch (DataAccessException ex) {
            ex.printStackTrace();
            try {
                db.closeConnection(false);
            } catch (DataAccessException e) {
                e.printStackTrace();
            }
            return new FillResult("Error: Internal server error.", false);
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
            try {
                db.closeConnection(false);
            } catch (DataAccessException e) {
                e.printStackTrace();
            }
            return new FillResult("Error: Invalid generations parameter", false);
        }
    }
}
