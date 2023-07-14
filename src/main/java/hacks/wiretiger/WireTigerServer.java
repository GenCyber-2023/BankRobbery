package hacks.wiretiger;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import hacks.Duplexer;
import hacks.MessageObserver;

/**
 * A {@link MessageObserver} that allows clients to connect and be notified
 * whenever the messenger(s) that it is observing send or receive a message.
 */
public class WireTigerServer implements Runnable, MessageObserver {
    /**
     * Logger used to log important messages.
     */
    private static final Logger LOGGER = 
        Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /**
     * The port on which the server is listening for interested clients.
     */
    public static final int WIRE_TIGER_PORT = 11241;

    /**
     * The server socket used to accept connections from interested clients.
     */
    private final ServerSocket server;

    /**
     * The set of connected clients.
     */
    private final Set<Duplexer> clients;

    /**
     * A sentinel boolean used to determine whether or not the server should
     * continue accepting client requests and broadcasting messages.
     */
    private boolean running;

    /**
     * Creates a new Wire Tiger server listening on the standard Wire Tiger 
     * port.
     * 
     * @throws IOException If there is an error setting up the server.
     */
    public WireTigerServer() throws IOException {
        this(new ServerSocket(WIRE_TIGER_PORT));
    }

    /**
     * Creates a Wire Tiger server that uses the specified server instead of
     * creating its own on the standard port.
     * 
     * @param server The server to use to accept connections from interested
     * clients.
     */
    public WireTigerServer(ServerSocket server) {
        this.server = server;
        this.clients = new HashSet<>();
        this.running = true;
    }

    /**
     * Closes the server. This will stop accepting new connections and will
     * close any existing connections.
     */
    public void close() {
        this.running = false;
        try {
            this.server.close();
        } catch (IOException e) {
            // squash
        }
    }

    @Override
    public void run() {
        LOGGER.info("WireTiger starting on port " + server.getLocalPort()
            + "...");
        while(running) {
            try {
                Socket socket = server.accept();
                addClient(socket);
            } catch (IOException e) {
                if(running) {
                    LOGGER.severe("Failed to accept a new client socket!");
                    running = false;
                }
            }
        }
        LOGGER.info("WireTiger shutting down.");
    }

    @Override
    public void messageReceived(InetAddress sender, int port, String message) {
        String info = "<< " + sender + ":" + port + ": >>" + message + "<<";
        broadcastInfo(info);
    }

    @Override
    public void messageSent(InetAddress recipient, int port, String message) {
        String info = ">> " + recipient + ":" + port + ": >>" + message + "<<";
        broadcastInfo(info);
    }

    /**
     * Attempts to add a newly connected client to the set of clients that
     * will receive updates about messages sent or received.
     * 
     * @param socket The socket used to communicate with the new client.
     */
    private void addClient(Socket socket) {
        try {
            Duplexer duplexer = new Duplexer(socket);
            clients.add(duplexer);
        } catch (IOException e) {
            LOGGER.warning("Failed to establish connection to client: " 
                + socket);
        }
    }

    /**
     * Attempts to broadcast a descriptive message to all currently connected
     * clients. If a dead client is found, it will be removed.
     * 
     * @param message The message to broadcast.
     */
    private void broadcastInfo(String message) {
        Set<Duplexer> cleanUp = new HashSet<>();
        for(Duplexer client : clients) {
            if(client.isOpen()) {
                client.send(message);
            } else {
                LOGGER.warning("Found a dead client (removing): " 
                    + client);
                cleanUp.add(client);
            }
        }
        clients.removeAll(cleanUp);
    }

    public static void main(String[] args) throws IOException {
        // FOR TESTING PURPOSES ONLY!!!
        WireTigerServer wts = new WireTigerServer();
        Thread thread = new Thread(wts);
        thread.start();

        boolean sentinel = true;
        try(java.util.Scanner scanner = new java.util.Scanner(System.in)) {
            while(sentinel) {
                System.out.print(">> ");
                String message = scanner.nextLine();
                if(message.equalsIgnoreCase("quit")) {
                    sentinel = false;
                } else {
                    wts.messageSent(wts.server.getInetAddress(), 
                        WIRE_TIGER_PORT, message);
                }
            }
        }
        wts.close();
        System.out.println("Goodbye!");
    }
}
