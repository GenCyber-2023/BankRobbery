package server;

import java.io.IOException;
import java.net.Socket;

import static bank.CaesarCipher.decrypt;
import static bank.CaesarCipher.encrypt;

public class BankClientHandler extends HandlerThread {

    protected BankClientHandler(Socket socket) throws IOException {
        super(socket);
    }

    @Override
    public String handleMessage(String message) {
        String plaintext = decrypt(message, BANK_SHIFT);
        String response = plaintext.toUpperCase();
        String ciphertext = encrypt(response, BANK_SHIFT);
        return ciphertext;
    }
    
}
