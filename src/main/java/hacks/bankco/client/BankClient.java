package hacks.bankco.client;

import static hacks.CaesarCipher.decrypt;
import static hacks.CaesarCipher.encrypt;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

import hacks.Duplexer;
import hacks.bankco.BankCo;

/**
 * Command line interface that will transmit encrypted messages to a server.
 */
public class BankClient extends Duplexer implements BankCo, Runnable {
    /**
     * The command that quits the client.
     */
    private static final String QUIT = "QUIT";

    /**
     * Creates a new client that communicates with the specified host on the
     * standard port.
     * 
     * @param host The hostname of the bank server.
     * @throws IOException
     */
    public BankClient(String host) throws IOException {
        this(new Socket(host, BANK_PORT));
    }

    public BankClient(Socket socket) throws IOException {
        super(socket);
    }

    @Override
    public void run() {
        System.out.println("Welcome to the BankCo Client. " 
            + "Enter a command to send to the BankCo server or QUIT to quit.");
        boolean sentinel = true;
        Scanner scanner = new Scanner(System.in);

        while(sentinel) {
            System.out.print(">>> ");
            String request = scanner.nextLine();

            if(request.equalsIgnoreCase(QUIT)) {
                sentinel = false;
            } else {
                try {
                    String ciphertext = encrypt(request, 
                        BANK_SHIFT);
                    send(ciphertext);
                    String response = read();
                    String plaintext = decrypt(response, 
                        BANK_SHIFT);
                    System.out.println(plaintext);
                } catch(IOException ioe) {
                    System.err.println("Unexpected error: " + ioe.getMessage() 
                        + "The connection will be closed.");
                    sentinel = false;
                }
            }
        }

        scanner.close();
        close();
    }

    public static void main(String[] args) {
        String host = args.length > 0 ? args[0] : "localhost";
        try {
            BankClient client = new BankClient(host);
            Thread thread = new Thread(client);
            thread.start();
        } catch(IOException ioe) {
            System.err.println("Could not establish a connection to " 
                + host + ":" + BANK_PORT);
        }
    }
}