package server;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import bank.BankCo;

import static bank.CaesarCipher.decrypt;
import static bank.CaesarCipher.encrypt;

public class BankClientHandler extends HandlerThread implements BankCo {
    private static final Logger LOGGER = 
        Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    protected BankClientHandler(Socket socket) throws IOException {
        super(socket);
    }

    @Override
    public String handleMessage(String message) {
        String plaintext = decrypt(message, BANK_SHIFT);
        log("REQUEST: " + plaintext);
        String response = plaintext.toUpperCase();
        log("RESPONSE: " + response);
        String ciphertext = encrypt(response, BANK_SHIFT);
        return ciphertext;
    }

    private void log(String message) {
        log(Level.INFO, message);
    }

    private void log(Level level, String message) {
        LOGGER.log(level, toString() + ": " + message);
    }
    
}
