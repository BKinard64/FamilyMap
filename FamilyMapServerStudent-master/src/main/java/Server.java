import com.sun.net.httpserver.HttpServer;

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

    private void registerHandlers() {}
}
