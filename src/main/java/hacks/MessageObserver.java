package hacks;

import java.net.InetAddress;

/**
 * An interface implemented by classes that wish to be notified whenever a
 * message is sent or received by a a messenger.
 */
public interface MessageObserver {
    /**
     * 
     * @param sender
     * @param port
     * @param message
     */
    public void messageReceived(InetAddress sender, int port, String message);

    /**
     * 
     * @param recipient
     * @param port
     * @param message
     */
    public void messageSent(InetAddress recipient, int port, String message);
}
