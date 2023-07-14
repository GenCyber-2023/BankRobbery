package hacks;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.Charset;

/**
 * Simplifies communication using text-based protocols over a socket.
 */
public class Duplexer extends Messenger implements AutoCloseable {
    /**
     * UTF-16 character set used for encoding messages sent and received.
     */
    private static final Charset UTF_16 = Charset.forName("UTF-16");


    /**
     * The socket used to communicate.
     */
    private final Socket socket;

    /**
     * The PrintWriter used to send text messages to the other end of the 
     * socket.
     */
    private final PrintWriter writer;

    /**
     * The buffered reader used to receive text messages from the other end of
     * the socket.
     */
    private final BufferedReader reader;

    /**
     * The string representation of the duplexer; useful for logging.
     */
    private final String stringRepresentation;

    /**
     * Creates a new duplexer that communicates using the specified socket.
     * 
     * @param socket The socket used to communicate.
     * 
     * @throws IOException If there is a problem trying to create the writer
     * or reader used to communicate over the socket, e.g. the socket has been
     * closed.
     */
    public Duplexer(Socket socket) throws IOException {
        this.socket = socket;
        this.writer = new PrintWriter(socket.getOutputStream(), true, UTF_16);
        this.reader = new BufferedReader(
            new InputStreamReader(socket.getInputStream(), UTF_16));

        this.stringRepresentation = "(" + socket.getLocalAddress() + ":" 
            + socket.getLocalPort() + ") <-> (" + socket.getInetAddress() 
            + ":" + socket.getPort() + ")";
    }

    /**
     * Sends a message to the other end of the connection using UTF-16 encoding.
     * 
     * @param message The text message to send to the other end of the 
     * connection.
     */
    public void send(String message) {
        writer.println(message);
        writer.flush();
        messageSent(socket.getInetAddress(), socket.getPort(), message);
    }

    /**
     * Returns true if the connection still appears to be open. May send a
     * false positive if the connection has been closed but no messages have
     * been since it was closed.
     * 
     * @return True if the connection is still open, and false otherwise.
     */
    public boolean isOpen() {
        // the print writer will return true if the last attempt to write to
        // the socket's output stream failed. This may be a false positive if
        // the socket was closed after the last write.
        return !writer.checkError();
    }

    /**
     * Reads a line of text from the socket using UTF-16 encoding and returns 
     * it.
     * 
     * @return The line of text read from the socket.
     * 
     * @throws IOException If there is an error reading from the socket.
     */
    public String read() throws IOException {
        String message = reader.readLine();
        messageReceived(socket.getInetAddress(), socket.getPort(), 
            message);
        return message;
    }

    @Override
    public void close() {
        try {
            socket.close();
            writer.close();
            reader.close();
        } catch(IOException ioe) {

        }
    }

    @Override
    public String toString() {
        return stringRepresentation;
    }
}
