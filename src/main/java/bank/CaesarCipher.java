package bank;

import java.util.Scanner;

/**
 * A simple implementation of a Caesar Cipher.
 */
public class CaesarCipher {
    /**
     * Encrypts a single character and returns it.
     * 
     * @param c The character to encrypt.
     * @param shift The amount by which the character should be shifted.
     * @return The encrypted character.
     */
    public static char encrypt(char c, int shift) {
        return (char)(c + shift);
    }

    /**
     * Decrypts a single character and returns it.
     * 
     * @param c The character to decrypt.
     * @param shift The amount by which the plaintext character was encrypted.
     * @return The decrypted character.
     */
    public static char decrypt(char c, int shift) {
        return encrypt(c, -shift);
    }

    /**
     * Enncrypts an entire string of plaintext and returns the ciphertext.
     * 
     * @param plaintext The plaintext to encrypt.
     * @param shift The amount by which the characters should be shifted.
     * @return The ciphertext.
     */
    public static String encrypt(String plaintext, int shift) {
        StringBuilder builder = new StringBuilder();
        for(char c : plaintext.toCharArray()) {
            char encrypted = encrypt(c, shift);
            builder.append(encrypted);
        }
        return builder.toString();
    }

    /**
     * Decrypts an entire string of ciphertext and returns the plaintext.
     * 
     * @param ciphertext The ciphertext to decrypt.
     * @param shift The amount by which the plaintext was originally shifted.
     * @return The plaintext.
     */
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
                    String reversed = decrypt(ciphertext, shift);
                    System.out.println("reversed: " + reversed);
                }
            }
        } catch(Exception e) {
            System.err.println("A fatal error has occurred: " + e.getMessage());
        }
    }
}
