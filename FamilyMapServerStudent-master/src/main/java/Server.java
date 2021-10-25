import com.sun.net.httpserver.HttpServer;
import handler.ClearHandler;
import handler.FileHandler;
import handler.LoginHandler;
import handler.PersonHandler;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Server {
    private static final int MAX_WAITING_CONNECTIONS = 12;
    private HttpServer server;

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
        // Create and install the HTTP handler for the person API
        server.createContext("/person", new PersonHandler());
        // Create and install the HTTP handler for the Web-site
        server.createContext("/", new FileHandler());
    }
}
