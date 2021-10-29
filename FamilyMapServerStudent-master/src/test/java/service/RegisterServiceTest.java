package service;

import com.google.gson.Gson;
import dao.*;
import jsondata.FemaleNames;
import jsondata.LocationData;
import jsondata.MaleNames;
import jsondata.Surnames;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.requests.RegisterRequest;
import service.results.RegisterResult;

import static org.junit.jupiter.api.Assertions.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;

public class RegisterServiceTest {
    private RegisterService service;
    private Database db;

    @BeforeEach
    public void setUp() throws FileNotFoundException, DataAccessException {
        db = new Database();
        db.openConnection();
        db.clearTables();
        db.closeConnection(true);

        Gson gson = new Gson();
        // Store location data
        Reader reader = new FileReader("json/locations.json");
        LocationData locData = (LocationData)gson.fromJson(reader, LocationData.class);
        // Store female name data
        reader = new FileReader("json/fnames.json");
        FemaleNames fmlNames = (FemaleNames)gson.fromJson(reader, FemaleNames.class);
        // Store male name data
        reader = new FileReader("json/mnames.json");
        MaleNames mlNames = (MaleNames)gson.fromJson(reader, MaleNames.class);
        // Store surname data
        reader = new FileReader("json/snames.json");
        Surnames srNames = (Surnames)gson.fromJson(reader, Surnames.class);

        service = new RegisterService(locData, fmlNames, mlNames, srNames);
    }

    @AfterEach
    public void tearDown() throws DataAccessException {
        db.openConnection();

        db.clearTables();

        db.closeConnection(true);
    }

    @Test
    public void registerMissingProperties() {
        RegisterRequest request = new RegisterRequest("Ben123", "1111", null, "Ben",
                                                      "Button", "m");
        RegisterResult result = service.register(request);

        // Service should fail because email property is missing
        assertFalse(result.isSuccess());
        assertEquals("Error: Request property missing.", result.getMessage());
    }

    @Test
    public void registerTakenUsername() throws DataAccessException {
        db.openConnection();

        User user = new User("Ben123", "1111", "ben@cs.byu.edu", "Ben",
                            "Button", "m", "123b");
        new UserDao(db.getConnection()).insert(user);

        db.closeConnection(true);

        RegisterRequest request = new RegisterRequest("Ben123", "2222", "ben@stats.byu.edu",
                                                     "Ben", "Franklin", "m");
        RegisterResult result = service.register(request);

        // Service should fail because the username is taken
        assertFalse(result.isSuccess());
        assertEquals("Error: Username already taken by another user.", result.getMessage());
    }

    @Test
    public void registerInvalidGenderParam() {
        RegisterRequest request = new RegisterRequest("Ben123", "1111", "ben@cs.byu.edu",
                                                    "Ben", "Button", "Male");
        RegisterResult result = service.register(request);

        // Service should fail because gender property is invalid
        assertFalse(result.isSuccess());
        assertEquals("Error: Internal server error.", result.getMessage());
    }

    @Test
    public void registerSuccess() throws DataAccessException {
        RegisterRequest request = new RegisterRequest("Ben123", "1111", "ben@cs.byu.edu",
                "Ben", "Button", "m");
        RegisterResult result = service.register(request);

        // Service should be successful
        assertTrue(result.isSuccess());
        assertNotNull(result.getAuthtoken());
        assertEquals(request.getUsername(), result.getUsername());
        assertNotNull(result.getPersonID());

        db.openConnection();
        UserDao uDao = new UserDao(db.getConnection());
        PersonDao pDao = new PersonDao(db.getConnection());
        EventDao eDao = new EventDao(db.getConnection());
        AuthTokenDao atDao = new AuthTokenDao(db.getConnection());

        assertNotNull(uDao.find(request.getUsername()));
        assertNotNull(pDao.getFamily(request.getUsername()));
        assertNotNull(eDao.getFamilyEvents(request.getUsername()));
        assertNotNull(atDao.find(result.getAuthtoken()));
        assertEquals(request.getUsername(), atDao.find(result.getAuthtoken()).getUsername());

        db.closeConnection(true);
    }
}
