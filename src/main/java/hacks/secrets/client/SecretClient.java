package hacks.secrets.client;

import java.io.IOException;
import java.net.Socket;
import java.util.InputMismatchException;
import java.util.Scanner;

import static hacks.CaesarCipher.decrypt;
import static hacks.CaesarCipher.encrypt;
import hacks.Duplexer;
import hacks.secrets.Secrets;

public class SecretClient extends Duplexer implements Secrets, Runnable {
    private final Scanner scanner;

    public SecretClient(Socket socket) throws IOException {
        super(socket);
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void run() {
        try(Scanner scanner = new Scanner(System.in)) {
            setSecret();
            boolean sentinel = true;
            while(sentinel) {

            }
        } catch(IOException ioe) {
            System.err.println("Error communicating with server: " 
                + ioe.getMessage());
        }
        System.out.println("Goodbye!");
    }

    @Override
    public void send(String message) {
        super.send(encrypt(message, SECRET_SHIFT));
    }

    @Override
    public String read() throws IOException {
        return decrypt(super.read(), SECRET_SHIFT);
    }

    private void setSecret() throws IOException {
        int shift = getShift();
        String secret = getSecret();
        String ciphertext = encrypt(secret, shift);
        send(SET_SECRET + " " + ciphertext);
        String response = read();
        if(response.equals(SECRET_SET)) {
            System.out.println("Secret set successfull!");
        } else {
            System.out.println("Secret has already been set.");
        }
    }

    private int getShift() {
        while(true) {
            try {
                System.out.print("Enter valid shift between 1-255: ");
                int shift = scanner.nextInt();
                if(shift > 0 && shift < 256) {
                    return shift;
                }
            } catch(InputMismatchException e) {
                System.err.println("Please enter a valid integer.");
            }
        }
    }

    private String getSecret() {
        while(true) {
            System.out.print("Enter a secret phrase between 10 and 100 "
                + "characters long: ");
            String secret = scanner.nextLine();
            if(secret.length() > 9 && secret.length() < 101) {
                return secret;
            } else {
                System.err.println("Please enter a valid secret.");
            }
        }
    }

    public static void main(String[] args) throws Exception {
        String host = args.length > 0 ? args[0] : "localhost";
        try(Socket socket = new Socket(host, SECRET_PORT)) {
            SecretClient client = new SecretClient(socket);
            Thread thread = new Thread(client);
            thread.start();
        } catch(IOException ioe) {
            System.err.println("Error connecting to server: "
                + ioe.getMessage());
        }
    }
    
}
