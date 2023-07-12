package bank;

/**
 * Provides useful constants needed by various parts of the program.
 */
public class Bank {
    /**
     * Port used by clients to connect to the bank to conduct transactions.
     */
    public static final int BANK_PORT = 10241;

    /**
     * Port used by clients to connect to the bank to read traffic.
     */
    public static final int WIRE_TIGER_PORT = 11241;

    /**
     * Port used by clients to get score updates.
     */
    public static final int SCORE_PORT = 12241;
}
