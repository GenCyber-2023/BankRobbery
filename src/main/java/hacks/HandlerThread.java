package hacks;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A thread that handles communication from a remote host. This thread assumes
 * that a symmetric request/response protocol is being used and always 
 * initiated by the other end of the connection.
 */
public abstract class HandlerThread extends Duplexer implements Runnable {
    private static final Logger LOGGER = 
        Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /**
     * Creates a new handler thread to handle communication using a symmetric
     * request/response protocol.
     * 
     * @param socket The socket used to communicate.
     * 
     * @throws IOException If there is a problem setting up the connection.
     */
    protected HandlerThread(Socket socket) throws IOException {
        super(socket);
    }

    @Override
    public void run() {
        boolean sentinel = true;
        while(sentinel) {
            try {
                String request = read();
                log("Received request: " + request);
                String response = handleMessage(request);
                log("Sending response: " + response);
                send(response);
            } catch (IOException ioe) {
                log(Level.SEVERE, "Error communicating: " + ioe.getMessage());
                sentinel = false;
            }
        }
        log("Shutting down.");
        close();
    }

    /**
     * Called whenever a request is received from the other end of the 
     * connection.
     * 
     * @param message The message received from the other end of the 
     * connection.
     * @return The message that should be sent back in response to the request.
     */
    public abstract String handleMessage(String message); 

    private void log(String message) {
        log(Level.INFO, message);
    }

    private void log(Level level, String message) {
        LOGGER.log(level, getRemoteAddress() + ": " + message);
    }
}
