package bank;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.net.Socket;

public class Duplexer extends BankComponent implements AutoCloseable {
    private final Socket socket;
    private final PrintWriter writer;
    private final BufferedReader reader;
    private final String string;


    public Duplexer(Socket socket) throws IOException {
        this.socket = socket;
        this.writer = new PrintWriter(socket.getOutputStream());
        this.reader = new BufferedReader(
            new InputStreamReader(socket.getInputStream()));

        this.string = "(" + socket.getLocalAddress() + ":" 
            + socket.getLocalPort() + ") <-> (" + socket.getInetAddress() 
            + ":" + socket.getPort() + ")";

        logMessage("Created");
    }

    public void send(String message) {
        logMessage(Level.INFO, "SENDING: " + message);
        writer.println(message);
        writer.flush();
    }

    public String read() throws IOException {
        String message = reader.readLine();
        logMessage("RECEIVED: " + message);
        return message;
    }

    @Override
    public void close() {
        logMessage("CLOSING");
        try {
            socket.close();
            writer.close();
            reader.close();
        } catch(IOException ioe) {
            logMessage(Level.WARNING, "Error closing socket: " 
                + ioe.getMessage());
        }
    }

    @Override
    public String toString() {
        return string;
    }

    protected void logMessage(String message) {
        logMessage(Level.INFO, message);
    }

    protected void logMessage(Level level, String message) {
        log(level, this + ": " + message);
    }
}
