package bank.wiretiger;

import java.io.IOException;
import java.net.Socket;

import bank.Duplexer;

public class WireTigerClient extends Duplexer implements Runnable {

    public WireTigerClient(Socket socket) throws IOException {
        super(socket);
    }

    @Override
    public void run() {
        
    }
    
}
