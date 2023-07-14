package hacks.secrets.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import hacks.HandlerThread;
import hacks.Messenger;
import hacks.secrets.Secrets;

public class SecretServer extends Messenger implements Secrets, Runnable {
    private static final Logger LOGGER = 
        Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    
    private final ServerSocket server;

    public SecretServer() throws IOException {
        this(new ServerSocket(SECRET_PORT));
    }

    public SecretServer(ServerSocket server) {
        this.server = server;
    }

    @Override
    public void run() {
        while(true) {
            try {
                LOGGER.log(Level.INFO, "Waiting for next client...");
                Socket client = server.accept();
                LOGGER.log(Level.INFO, "Client connected; spinning up handler: " 
                    + client.getInetAddress());
                HandlerThread handler = new SecretClientHandler(client);
                handler.setOnMessage(getObserver());
                Thread thread = new Thread(handler);
                thread.start();
            } catch(IOException ioe) {
                LOGGER.warning("Failed to accept a new client!");
                break;
            }
        }
        LOGGER.info("Server is shutting down.");
    }
    
}
