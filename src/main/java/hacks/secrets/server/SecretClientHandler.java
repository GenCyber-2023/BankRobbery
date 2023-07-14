package hacks.secrets.server;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import static hacks.CaesarCipher.decrypt;
import static hacks.CaesarCipher.encrypt;
import hacks.HandlerThread;
import hacks.secrets.Secrets;

public class SecretClientHandler extends HandlerThread implements Secrets {
    /**
     * Used to log messages.
     */
    private static final Logger LOGGER = 
        Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    protected SecretClientHandler(Socket socket) throws IOException {
        super(socket);
        log("Created for " + getRemoteAddress());
    }

    @Override
    public String handleMessage(String ciphertext) {
        String message = decrypt(ciphertext, SECRET_SHIFT);
        log("Handling message: " + message);
        String response;        
        try {
            String[] tokens = message.split(" ", 2);
            switch(tokens[0]) {
                case SET_SECRET:
                    response = tryToSetSecret(tokens[1]);
                    break;
                case GUESS_SECRET:
                    response = guessSecret(tokens[1]);
                    break;
                default:
                    response = invalidRequest(message);
                    break;
            }
        } catch(Exception exception) {
            log(Level.WARNING, exception.getMessage());
            response = invalidRequest(message);
        }
        log("Returning response: " + response);
        return encrypt(response, SECRET_SHIFT);
    }
    
    private String tryToSetSecret(String secret) {
        return Secrets.setSecret(getRemoteAddress(), secret) ?
            SECRET_SET : SECRET_ALREADY_SET;
    }

    private String guessSecret(String request) throws Exception {
        String[] tokens = request.split(" ", 3);
        String host = tokens[0];
        int shift = Integer.parseInt(tokens[1]);
        String guess = tokens[2];

        return Secrets.guessSecret(host, shift, guess) ?
            GUESS_WAS_CORRECT : GUESS_WAS_INCORRECT;   
    }

    private String invalidRequest(String message) {
        return INVALID_REQUEST + " " + message;
    }

    private void log(String message) {
        log(Level.INFO, message);
    }

    private void log(Level level, String message) {
        LOGGER.log(level, getRemoteAddress() + ": " + message);
    }
}
