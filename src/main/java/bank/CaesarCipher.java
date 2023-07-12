package bank;

import java.util.Scanner;

/**
 * A simple implementation of a Caesar Cipher.
 */
public class CaesarCipher {
    public static char encrypt(char c, int shift) {
        return (char)(c + shift);
    }

    public static char decrypt(char c, int shift) {
        return encrypt(c, -shift);
    }

    public static String encrypt(String plaintext, int shift) {
        StringBuilder builder = new StringBuilder();
        for(char c : plaintext.toCharArray()) {
            char encrypted = encrypt(c, shift);
            builder.append(encrypted);
        }
        return builder.toString();
    }

    public static String decrypt(String ciphertext, int shift) {
        return encrypt(ciphertext, -shift);
    }

    public static void main(String[] args) {
        try(Scanner scanner = new Scanner(System.in)) {
            System.out.print("Enter shift >> ");
            int shift = scanner.nextInt();
            scanner.nextLine();

            boolean sentinel = true;
            while(sentinel) {
                System.out.print("Enter plaintext: ");
                String plaintext = scanner.nextLine();
                if(plaintext.equals("")) {
                    sentinel = false;
                } else {
                    String ciphertext = encrypt(plaintext, shift);
                    System.out.println("ciphertext: " + ciphertext);
                }
            }
        } catch(Exception e) {
            System.err.println("A fatal error has occurred: " + e.getMessage());
        }
    }
}
