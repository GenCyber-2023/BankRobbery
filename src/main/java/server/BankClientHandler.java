package server;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;

import bank.Duplexer;

public class BankClientHandler extends Duplexer implements Runnable {

    public BankClientHandler(Socket socket) throws IOException {
        super(socket);
    }

    @Override
    public void run() {
        boolean sentinel = true;
        while(sentinel) {
            try {
                String request = read();
                send(request.toUpperCase());
            } catch (IOException ioe) {
                log(Level.SEVERE, "Error communicatinig with client: "
                    + ioe.getMessage());
                sentinel = false;
            }
        }

        close();
    }
    
}
