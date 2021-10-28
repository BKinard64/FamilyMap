import com.google.gson.Gson;
import com.sun.net.httpserver.HttpServer;
import handler.*;
import jsondata.FemaleNames;
import jsondata.LocationData;
import jsondata.MaleNames;
import jsondata.Surnames;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.net.InetSocketAddress;

public class Server {
    private static final int MAX_WAITING_CONNECTIONS = 12;
    private HttpServer server;
    private LocationData locData;
    private FemaleNames fmlNames;
    private MaleNames mlNames;
    private Surnames srNames;

    public static void main(String[] args) {
        int port = Integer.parseInt(args[0]);
        new Server().run(port);
    }

    private void run(int port) {
        System.out.println("Initializing HTTP Server");
        // Store the IP Address and Port # the server will listen on
        InetSocketAddress serverAddress = new InetSocketAddress(port);
        try {
            // Initialize the server
            server = HttpServer.create(serverAddress, MAX_WAITING_CONNECTIONS);
        }
        catch (IOException e) {
            e.printStackTrace();
            return;
        }

        // Cache JSON data for family tree generation
        try {
            cacheJsonData();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        // Map urls to appropriate handler classes
        registerHandlers();



        System.out.println("Starting server");
        // Have the server start receiving incoming client connections
        server.start();

        System.out.println("Server started. Listening on port " + port);
    }

    private void registerHandlers() {
        System.out.println("Creating contexts");
        // Create and install the HTTP handler for the load API
        server.createContext("/user/login", new LoginHandler());
        // Create and install the HTTP handler for the clear API
        server.createContext("/clear", new ClearHandler());
        // Create and install the HTTP handler for the fill API
        server.createContext("/fill", new FillHandler(locData, fmlNames, mlNames, srNames));
        // Create and install the HTTP handler for the load API
        server.createContext("/load", new LoadHandler());
        // Create and install the HTTP handler for the person API
        server.createContext("/person", new PersonHandler());
        // Create and install the HTTP handler for the event API
        server.createContext("/event", new EventHandler());
        // Create and install the HTTP handler for the Web-site
        server.createContext("/", new FileHandler());
    }

    private void cacheJsonData() throws IOException {
        System.out.println("Storing JSON Data for family tree generation");
        Gson gson = new Gson();
        // Store location data
        Reader reader = new FileReader("json/locations.json");
        locData = (LocationData)gson.fromJson(reader, LocationData.class);
        // Store female name data
        reader = new FileReader("json/fnames.json");
        fmlNames = (FemaleNames)gson.fromJson(reader, FemaleNames.class);
        // Store male name data
        reader = new FileReader("json/mnames.json");
        mlNames = (MaleNames)gson.fromJson(reader, MaleNames.class);
        // Store surname data
        reader = new FileReader("json/snames.json");
        srNames = (Surnames)gson.fromJson(reader, Surnames.class);
    }
}
