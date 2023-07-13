package bank.bankco.server.util;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import static bank.CaesarCipher.encrypt;

import bank.bankco.server.CsvUserDatabase;
import bank.bankco.server.User;

public class RandomUserMaker {
    private static final Random RNG = new Random();

    public static Collection<User> makeRandomUsers(int numberOfUsers, 
        int passwordLength) {
        
        Set<User> users = new HashSet<>();

        while(users.size() < numberOfUsers) {
            int accountNumber = RNG.nextInt(1000000000, Integer.MAX_VALUE);
            int shift = RNG.nextInt(1, 10);
            String password = makeRandomPassword(passwordLength);
            String encryptedPassword = encrypt(password, shift);

            User user = new User(User.Type.CUSTOMER, accountNumber, 
                encryptedPassword, shift);
            users.add(user);
        }

        return users;
    }

    private static String makeRandomPassword(int length) {
        StringBuilder builder = new StringBuilder();
        for(int i=0; i<length; i++) {
            char c = (char)RNG.nextInt(33, 127);
            builder.append(c);
        }
        return builder.toString();
    }

    public static void main(String[] args) throws IOException {
        String filename = args.length > 0 ? args[0] : "data/users.csv";

        Collection<User> users = makeRandomUsers(40, 10);
        CsvUserDatabase userDatabase = new CsvUserDatabase(users);
        userDatabase.save(filename);
    }
}
