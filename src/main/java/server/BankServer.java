package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class BankServer implements Runnable {
    private final ServerSocket server;

    public BankServer(ServerSocket server) {
        this.server = server;
    }

    @Override
    public void run() {
        while(true) {
            try {
                Socket client = server.accept();
                BankClientHandler handler = new BankClientHandler(client);
                Thread thread = new Thread(handler);
                thread.start();
            } catch (IOException e) {
                // squash?
            }
        }
    }

    public static void main(String[] args) {
        // foo
    }
    
}
