package bank.bankco.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import bank.HandlerThread;
import bank.bankco.BankCo;

public class BankServer implements BankCo, Runnable {
    private static final Logger LOGGER = 
        Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    
    private final ServerSocket server;

    public BankServer() throws IOException {
        this(new ServerSocket(BANK_PORT));
    }

    public BankServer(ServerSocket server) {
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
                HandlerThread handler = new BankClientHandler(client);
                Thread thread = new Thread(handler);
                thread.start();
            } catch (IOException e) {
                LOGGER.log(Level.WARNING, "An error occurred: " 
                    + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        try {
            BankServer server = new BankServer();
            Thread thread = new Thread(server);
            thread.start();
        } catch(IOException ioe) {
            LOGGER.severe("Failed to start server: " + ioe.getMessage());
        }
    }    
}
