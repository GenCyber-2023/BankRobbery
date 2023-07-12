package bank;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.logging.Logger;
import java.net.Socket;

public class Duplexer implements AutoCloseable {
    private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    private final Socket socket;
    private final PrintWriter writer;
    private final BufferedReader reader;

    public Duplexer(Socket socket) throws IOException {
        this.socket = socket;
        this.writer = new PrintWriter(socket.getOutputStream());
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public void send(String message) {
        LOGGER.info("SENDING: " + message);
        writer.println(message);
        writer.flush();
    }

    public String read() throws IOException {
        String message = reader.readLine();
        LOGGER.info("RECEIVED: " + message);
        return message;
    }

    @Override
    public void close() {
        LOGGER.info("CLOSING");
        try {
            socket.close();
            writer.close();
            reader.close();
        } catch(IOException ioe) {
            // squash
        }
    }
}
