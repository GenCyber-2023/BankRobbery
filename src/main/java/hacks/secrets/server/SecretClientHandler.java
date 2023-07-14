package hacks.secrets.server;

import java.io.IOException;
import java.net.Socket;

import hacks.HandlerThread;

public class SecretClientHandler extends HandlerThread {

    protected SecretClientHandler(Socket socket) throws IOException {
        super(socket);
    }

    @Override
    public String handleMessage(String message) {
        return "placeholder!";
    }
    
}
