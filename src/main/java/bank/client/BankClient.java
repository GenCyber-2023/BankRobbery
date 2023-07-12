package bank.client;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

import bank.Duplexer;

public class BankClient extends Duplexer implements Runnable {
    private static final int PORT = 10240;
    private static final String QUIT = "QUIT";

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
                    System.err.println("Unexpected error: " + ioe.getMessage());
                    System.err.println("The connection will be closed.");
                    sentinel = false;
                }
            }
        }

        scanner.close();
        close();
    }

    public static void main(String[] args) {
        
    }
}