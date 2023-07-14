package hacks;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.Charset;

public class Duplexer implements AutoCloseable {
    private static final Charset UTF_16 = Charset.forName("UTF-16");


    private final Socket socket;
    private final PrintWriter writer;
    private final BufferedReader reader;
    private final String string;

    private MessageObserver observer;

    public Duplexer(Socket socket) throws IOException {
        this.socket = socket;
        this.writer = new PrintWriter(socket.getOutputStream(), true, UTF_16);
        this.reader = new BufferedReader(
            new InputStreamReader(socket.getInputStream(), UTF_16));

        this.string = "(" + socket.getLocalAddress() + ":" 
            + socket.getLocalPort() + ") <-> (" + socket.getInetAddress() 
            + ":" + socket.getPort() + ")";

        this.observer = null;
    }

    public void setOnMessage(MessageObserver observer) {
        this.observer = observer;
    }

    public void send(String message) {
        writer.println(message);
        writer.flush();
        if(observer != null) {
            observer.messageSent(socket.getInetAddress(), socket.getPort(), 
                message);
        }
    }

    public boolean isOpen() {
        return !writer.checkError();
    }

    public String read() throws IOException {
        String message = reader.readLine();
        if(observer != null) {
            observer.messageReceived(socket.getInetAddress(), socket.getPort(), 
                message);
        }
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
        return string;
    }
}
