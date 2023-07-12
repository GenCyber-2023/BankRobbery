package bank.bankco.server;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import bank.HandlerThread;
import bank.bankco.BankCo;

import static bank.CaesarCipher.decrypt;
import static bank.CaesarCipher.encrypt;

/**
 * Handles BankCo user requests from a single client.
 */
public class BankClientHandler extends HandlerThread implements BankCo {
    /**
     * An empty string.
     */
    private static final String EMPTY = "";

    /**
     * Used to log messages.
     */
    private static final Logger LOGGER = 
        Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /**
     * Creates a new client handler.
     * @param socket The socket used to communicate with the client.
     * @throws IOException If there is an error.
     */
    protected BankClientHandler(Socket socket) throws IOException {
        super(socket);
    }

    @Override
    public String handleMessage(String message) {
        String plaintext = decrypt(message, BANK_SHIFT);
        String request = plaintext.strip();

        log("REQUEST: >>" + plaintext + "<<");

        String[] tokens = request.split(" ");
        String command = tokens.length > 0 ? tokens[0] : "";

        String response;

        switch(command.toLowerCase()) {
            case LOGIN:
                response = login(tokens);
                break;
            case TRANSFER:
                response = transfer(tokens);
                break;
            case QUIT:
                response = quit();
                break;
            case EMPTY:
            default:
                response = unrecognized(request);
                break;
        }

        log("RESPONSE: " + response);
        String ciphertext = encrypt(response, BANK_SHIFT);

        String reversed = decrypt(ciphertext, BANK_SHIFT);
        log("DECRYPTED RESPONSE: " + reversed);

        return ciphertext;
    }

    private String login(String[] tokens) {
        return LOGGED_IN;
    }

    private String transfer(String[] tokens) {
        return TRANSFERRED;
    }

    private String quit() {
        return GOODBYE;
    }

    private String unrecognized(String request) {
        return UNRECOGNIZED + ": " + request;
    }

    private void log(String message) {
        log(Level.INFO, message);
    }

    private void log(Level level, String message) {
        LOGGER.log(level, toString() + ": " + message);
    }
    
}
