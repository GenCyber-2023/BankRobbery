package hacks.secrets;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import hacks.CaesarCipher;

public interface Secrets {
    public static final int SECRET_PORT = 13241;

    public static final String SET_SECRET = "SET_SECRET";
    public static final String SECRET_SET = "SECRET_SET";
    public static final String SECRET_ALREADY_SET = "SECRET_ALREADY_SET";

    public static final String GUESS_SECRET = "GUESS_SECRET";
    public static final String GUESS_WAS_CORRECT = "GUESS_WAS_CORRECT";
    public static final String GUESS_WAS_INCORRECT = "GUESS_WAS_INCORRECT";

    public static final String INVALID_REQUEST = "INVALID_REQUEST";

    public static final Map<InetAddress, String> SECRETS = new HashMap<>();

    public static boolean setSecret(InetAddress address, String secret) {
        boolean added;
        if(SECRETS.containsKey(address)) {
            added = false;
        } else {
            SECRETS.put(address, secret);
            added = true;
        }
        return added;
    }

    public static boolean guessSecret(String host, int shift, String guess) {
        boolean correct;
        try {
            InetAddress address = InetAddress.getByName(host);
            if(SECRETS.containsKey(address)) {
                String ciphertext = CaesarCipher.encrypt(guess, shift);
                String secret = SECRETS.get(address);
                correct = ciphertext.equals(secret);
            } else {
                correct = false;
            }
        } catch (UnknownHostException e) {
            correct = false;
        }  
    
        return correct;
    }
}
