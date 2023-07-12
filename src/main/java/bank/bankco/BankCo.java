package bank.bankco;

import java.nio.charset.Charset;

/**
 * Contains the common global variables needed by the BankCo client and server.
 */
public interface BankCo {
    /**
     * The UTF-16 character set, used to encode and decode non-ASCII characters.
     */
    public static final Charset UTF_16 = Charset.forName("UTF-16");

    /**
     * The shift used to encrypt/decrypt text sent to the main bank API.
     */
    public static final int BANK_SHIFT = 7;

    /**
     * Port used by clients to connect to the bank to conduct transactions.
     */
    public static final int BANK_PORT = 10241;

    /**
     * Request that represents a login attempt.
     */
    public static final String LOGIN = "login";

    /**
     * Request that represents a transfer request.
     */
    public static final String TRANSFER = "transfer";

    /**
     * Request sent before terminating the connection.
     */
    public static final String QUIT = "quit";

    /**
     * Response whena  login request is successful.
     */
    public static final String LOGGED_IN = "logged_in";

    /**
     * Response when a transfer is successful.
     */
    public static final String TRANSFERRED = "transferred";

    /**
     * Response to a quit request.
     */
    public static final String GOODBYE = "goodbye";

    /**
     * Response to an unrecognized command.
     */
    public static final String UNRECOGNIZED = "unrecognized";

    /**
     * Response when there is an error of any kind.
     */
    public static final String ERROR = "error";
}
