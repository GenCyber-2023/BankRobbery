package server;

import java.io.IOException;
import java.net.Socket;

import bank.Duplexer;

public class BankClientHandler extends Duplexer implements Runnable {

    public BankClientHandler(Socket socket) throws IOException {
        super(socket);
    }

    @Override
    public void run() {
    }
    
}
