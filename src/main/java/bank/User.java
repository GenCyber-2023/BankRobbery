package bank;

import static bank.CaesarCipher.encrypt;

public class User implements BankCo, Comparable<User> {
    public enum Type {
        CUSTOMER,
        BANKER,
        ADMIN
    }

    private final Type type;
    private final int accountNumber;
    private final String password;
    private final int shift;

    public User(Type type, int accountNumber, String password, int shift) {
        this.type = type;
        this.accountNumber = accountNumber;
        this.password = password;
        this.shift = shift;
    }

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
