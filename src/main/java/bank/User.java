package bank;

import static bank.CaesarCipher.encrypt;

import bank.bankco.BankCo;

/**
 * Represents a bank user.
 */
public class User implements BankCo, Comparable<User> {
    /**
     * The available user types. Some users can execute commands that others
     * are not allowed to execute.
     */
    public enum Type {
        CUSTOMER,
        BANKER,
        ADMIN
    }

    /**
     * The type of user.
     */
    private final Type type;

    /**
     * The unique account number for this user.
     */
    private final int accountNumber;

    /**
     * The user's password (encrypted).
     */
    private final String password;

    /**
     * The shift used to encrypt the user's password.
     */
    private final int shift;

    /**
     * Creates a new user with the given attributes.
     * 
     * @param type The user type.
     * @param accountNumber The unique account number for this user.
     * @param password The user's encrypted password.
     * @param shift The shift used to encrypt the password.
     */
    public User(Type type, int accountNumber, String password, int shift) {
        this.type = type;
        this.accountNumber = accountNumber;
        this.password = password;
        this.shift = shift;
    }

    /**
     * Returns true if the attempted password matches the user's real password,
     * and false otherwise.
     */
    public boolean authenticate(String attemptedPassword) {
        String encrypted = encrypt(attemptedPassword, shift);
        return encrypted.equals(password);
    }

    public Type getType() {
        return type;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public String getPassword() {
        return password;
    }

    public int getShift() {
        return shift;
    }

    @Override
    public String toString() {
        return "User [type=" + type + ", accountNumber=" + accountNumber + "]";
    }

    @Override
    public int compareTo(User other) {
        return this.accountNumber - other.accountNumber;
    }
}
