package service;

import com.google.gson.Gson;
import dao.DataAccessException;
import dao.Database;
import dao.UserDao;
import jsondata.FemaleNames;
import jsondata.LocationData;
import jsondata.MaleNames;
import jsondata.Surnames;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import requests.FillRequest;
import results.FillResult;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;

import static org.junit.jupiter.api.Assertions.*;

public class FillServiceTest {
    private FillService service;
    private Database db;
    private User user;

    @BeforeEach
    public void setUp() throws FileNotFoundException, DataAccessException {
        db = new Database();
        db.openConnection();
        db.clearTables();
        db.closeConnection(true);

        user = new User("Ben123", "1111", "ben@cs.byu.edu", "Ben", "Button",
                        "m", "123b");

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

        service = new FillService(locData, fmlNames, mlNames, srNames);
    }

    @AfterEach
    public void tearDown() throws DataAccessException {
        db.openConnection();

        db.clearTables();

        db.closeConnection(true);
    }

    @Test
    public void fillInvalidUsername() {
        FillRequest request = new FillRequest("Ben123", "4");
        FillResult result = service.fill(request);
        // The service should fail because the database is empty
        assertFalse(result.isSuccess());
        assertEquals("Error: Invalid username.", result.getMessage());
    }

    @Test
    public void fillInvalidGeneration() throws DataAccessException {
        new UserDao(db.getConnection()).insert(user);
        db.closeConnection(true);

        FillRequest request = new FillRequest("Ben123", "?");
        FillResult result = service.fill(request);

        // The service should fail because the generation parameter is invalid
        assertFalse(result.isSuccess());
        assertEquals("Error: Invalid generations parameter.", result.getMessage());

        request = new FillRequest("Ben123", "-2");
        result = service.fill(request);

        // The service should fail because the generation parameter is invalid
        assertFalse(result.isSuccess());
        assertEquals("Error: Invalid generations parameter.", result.getMessage());
    }

    @Test
    public void fill3Success() throws DataAccessException {
        new UserDao(db.getConnection()).insert(user);
        db.closeConnection(true);

        FillRequest request = new FillRequest("Ben123", "3");
        FillResult result = service.fill(request);

        // The service should be successful
        assertTrue(result.isSuccess());
        assertEquals("Successfully added 15 persons and 43 events to the database.", result.getMessage());
    }

    @Test
    public void fill4Success() throws DataAccessException {
        new UserDao(db.getConnection()).insert(user);
        db.closeConnection(true);

        FillRequest request = new FillRequest("Ben123", "4");
        FillResult result = service.fill(request);

        // The service should be successful
        assertTrue(result.isSuccess());
        assertEquals("Successfully added 31 persons and 91 events to the database.", result.getMessage());
    }

    @Test
    public void fill5Success() throws DataAccessException {
        new UserDao(db.getConnection()).insert(user);
        db.closeConnection(true);

        FillRequest request = new FillRequest("Ben123", "5");
        FillResult result = service.fill(request);

        // The service should be successful
        assertTrue(result.isSuccess());
        assertEquals("Successfully added 63 persons and 187 events to the database.", result.getMessage());
    }
}
