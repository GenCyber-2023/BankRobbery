package hacks.secrets.server;

import java.io.IOException;
import java.net.Socket;

import hacks.HandlerThread;
import hacks.secrets.Secrets;

public class SecretClientHandler extends HandlerThread implements Secrets {
    protected SecretClientHandler(Socket socket) throws IOException {
        super(socket);
    }

    @Override
    public String handleMessage(String message) {
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
            response = invalidRequest(message);
        }

        return response;
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
