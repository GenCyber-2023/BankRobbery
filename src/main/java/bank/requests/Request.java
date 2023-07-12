package bank.requests;

import bank.User;

/**
 * The interface for one of the commands that may be executed via the API.
 */
public interface Request {
    /**
     * Attempts to execute this command for the specified user.
     */
    public String execute(User user, String... tokens);
}
