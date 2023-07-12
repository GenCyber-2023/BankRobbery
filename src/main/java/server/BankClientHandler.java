package server;

import java.io.IOException;
import java.net.Socket;

public class BankClientHandler extends HandlerThread {

    protected BankClientHandler(Socket socket) throws IOException {
        super(socket);
    }

    @Override
    public String handleMessage(String message) {
        return message.toUpperCase();
    }
    
}
