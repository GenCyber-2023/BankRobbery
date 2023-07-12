package bank;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Provides useful constants needed by various parts of the program.
 */
public class Bank {
    /**
     * Can be used to log messages.
     */
    private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

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

    public static void log(Level level, String message) {
        LOGGER.log(level, message);
    }
}
