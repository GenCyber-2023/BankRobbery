package bank.bankco.server;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class UserDatabase {
    private final Map<Integer, User> users;

    public UserDatabase(Collection<User> users) {
        this.users = new HashMap<>();
        for(User user : users) {
            addUser(user);
        }
    }

    public void addUser(User user) {
        users.put(user.getAccountNumber(), user);
    }

    public void addUsers(User... users) {
        for(User user : users) {
            addUser(user);
        }
    }

    public User getUser(int accountNumber) {
        return users.get(accountNumber);
    }

    public Collection<User> getUsers() {
        return users.values();
    }
}
