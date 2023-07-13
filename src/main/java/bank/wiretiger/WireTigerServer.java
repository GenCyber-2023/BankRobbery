package bank.wiretiger;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import bank.Duplexer;
import bank.MessageObserver;

public class WireTigerServer implements Runnable, MessageObserver {
    private static final Logger LOGGER = 
        Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    public static final int WIRE_TIGER_PORT = 11241;

    private final ServerSocket server;
    private final Set<Duplexer> clients;
    private boolean running;

    public WireTigerServer() throws IOException {
        this(new ServerSocket(WIRE_TIGER_PORT));
    }

    public WireTigerServer(ServerSocket server) {
        this.server = server;
        this.clients = new HashSet<>();
        this.running = true;
    }

    public void close() {
        this.running = false;
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
                LOGGER.severe("Failed to accept a new client socket!");
                running = false;
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

    private void addClient(Socket socket) {
        try {
            Duplexer duplexer = new Duplexer(socket);
            clients.add(duplexer);
        } catch (IOException e) {
            LOGGER.warning("Failed to establish connection to client: " 
                + socket);
        }
    }

    private void broadcastInfo(String message) {
        for(Duplexer client : clients) {
            client.send(message);
        }
    }
}
