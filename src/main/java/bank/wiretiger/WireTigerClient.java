package bank.wiretiger;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Logger;

import bank.Duplexer;

public class WireTigerClient extends Duplexer implements Runnable {
    private static final Logger LOGGER = 
        Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    private boolean running;

    public WireTigerClient(Socket socket) throws IOException {
        super(socket);
        running = true;
    }

    @Override
    public void run() {
        LOGGER.info("WireTiger client is running.");
        while(running) {
            try {
                String message = read();
                LOGGER.info(message);
            } catch (IOException ioe) {
                LOGGER.severe("Error reading from WireTiger server: " 
                    + ioe.getMessage());
                running = false;
            }
        }
        LOGGER.info("Shutting down...");
    }

    public static void main(String[] args) throws IOException {
        String host = args.length > 0 ? args[0] : "localhost";
        Socket socket = new Socket(host, WireTigerServer.WIRE_TIGER_PORT);
        WireTigerClient client = new WireTigerClient(socket);
        Thread thread = new Thread(client);
        thread.start();
    }
}
