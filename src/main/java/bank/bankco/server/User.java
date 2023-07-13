package bank.bankco.server;

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
     * The user's encrypted password.
     */
    private final String encryptedPassword;

    /**
     * The shift used to encrypt the user's password.
     */
    private final int shift;

    /**
     * Creates a new user with the given attributes.
     * 
     * @param type The user type.
     * @param accountNumber The unique account number for this user.
     * @param encryptedPassword The user's encrypted password.
     * @param shift The shift used to encrypt the password.
     */
    public User(Type type, int accountNumber, String encryptedPassword, 
                int shift) {
        this.type = type;
        this.accountNumber = accountNumber;
        this.encryptedPassword = encryptedPassword;
        this.shift = shift;
    }

    /**
     * Returns true if the attempted password matches the user's real password,
     * and false otherwise.
     */
    public boolean authenticate(String attempt) {
        String encryptedAttempt = encrypt(attempt, shift);
        return encryptedAttempt.equals(encryptedPassword);
    }

    public Type getType() {
        return type;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public String getEncryptedPassword() {
        return encryptedPassword;
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

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof User) {
            User other = (User)obj;
            return this.accountNumber == other.accountNumber;
        } else {
            return false;
        }
    }
}
