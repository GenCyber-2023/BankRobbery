package server;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;

import bank.Duplexer;

public abstract class HandlerThread extends Duplexer implements Runnable {

    protected HandlerThread(Socket socket) throws IOException {
        super(socket);
    }

    @Override
    public void run() {
        boolean sentinel = true;
        while(sentinel) {
            try {
                String request = read();
                String response = handleMessage(request);
                send(response);
            } catch (IOException ioe) {
                log(Level.SEVERE, "Error communicatinig with client: "
                    + ioe.getMessage());
                sentinel = false;
            }
        }

        close();
    }

    public abstract String handleMessage(String message); 
}
