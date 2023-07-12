package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;

import bank.BankComponent;

public class BankServer extends BankComponent implements Runnable {
    
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
                log("Waiting for next client...");
                Socket client = server.accept();
                log("Client connected; spinning up handler: " 
                    + client.getInetAddress());
                HandlerThread handler = new BankClientHandler(client);
                Thread thread = new Thread(handler);
                thread.start();
            } catch (IOException e) {
                log(Level.WARNING, "An error occurred: " + e.getMessage());
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
