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
            String command = getFirstWord(message);
            String rest = substringAfterFirstSpace(message);
            switch(command) {
                case SET_SECRET:
                    response = tryToSetSecret(rest);
                    break;
                case GUESS_SECRET:
                    response = guessSecret(rest);
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

    private String guessSecret(String guess) throws Exception {
        int shift = Integer.parseInt(getFirstWord(guess));
        guess = substringAfterFirstSpace(guess);
        String host = getFirstWord(guess);
        guess = substringAfterFirstSpace(guess);
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

    private static String getFirstWord(String string) {
        int indexOfFirstSpace = string.indexOf(' ');
        return string.substring(0, indexOfFirstSpace);
    }

    private static String substringAfterFirstSpace(String string) {
        String split = null;
        int indexOfFirstSpace = string.indexOf(' ');
        if(indexOfFirstSpace > -1) {
            split = string.substring(indexOfFirstSpace + 1);
        }
        return split;
    }
}
