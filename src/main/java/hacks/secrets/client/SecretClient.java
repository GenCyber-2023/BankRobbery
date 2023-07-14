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
                System.out.println(">> ");
                String command = scanner.nextLine();
                String[] tokens = command.split(" ", 2);
                switch(tokens[0]) {
                    case "guess":
                        guess(tokens[1]);
                        break;
                    case "decrypt":
                        tryDecrypt(tokens[1]);
                        break;
                    case "help":
                        help();
                        break;
                    default:
                        System.err.println("IDK what that means. Try 'help'");
                }
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

    private void guess(String command) throws IOException {
        try {
            String[] tokens = command.split(" ", 3);
            String host = tokens[0];
            int shift = Integer.parseInt(tokens[1]);
            String secret = tokens[2];
            send(GUESS_SECRET + " " + host + " " + shift + " " + secret);
            String response = read();
            System.out.println(response);
        } catch(IOException ioe) {
            throw ioe;
        } catch(Exception e) {
            System.err.println("Try again: guess <host> <shift> <secret>");
        }

    }

    private void tryDecrypt(String command) {
        try {
            String[] tokens = command.split(" ", 2);
            int shift = Integer.parseInt(tokens[0]);
            String ciphertext = tokens[1];
            String plaintext = decrypt(ciphertext, shift);
            System.out.println("Plaintext: " + plaintext);
        } catch(Exception e) {
            System.err.println("Try again: decrypt <shift> <ciphertext>");
        }
    }

    private void help() {
        System.out.println("Enter one of the following commands: ");
        System.out.println("guess <hostname or IP> <shift> <secret>");
        System.out.println("decrypt <shift> <ciphertext>");
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
                scanner.nextLine(); // clear out the buffer
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
