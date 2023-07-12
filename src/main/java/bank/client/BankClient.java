package bank.client;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;

import bank.Bank;
import bank.Duplexer;

public class BankClient extends Duplexer implements Runnable {
    private static final String QUIT = "QUIT";

    public BankClient(String host) throws IOException {
        this(new Socket(host, Bank.BANK_PORT));
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
                    send(request);
                    String response = read();
                    System.out.println(response);
                } catch(IOException ioe) {
                    log(Level.SEVERE, "Unexpected error: " + ioe.getMessage() 
                        + "The connection will be closed.");
                    sentinel = false;
                }
            }
        }

        scanner.close();
        close();

        log(Level.INFO, "Goodbye!");
    }

    public static void main(String[] args) {
        String host = args.length > 0 ? args[0] : "localhost";
        try {
            BankClient client = new BankClient(host);
            Thread thread = new Thread(client);
            thread.start();
        } catch(IOException ioe) {
            Bank.log(Level.SEVERE, "Could not establish a connection to " 
                + host + ":" + Bank.BANK_PORT)   ;
        }
    }
}