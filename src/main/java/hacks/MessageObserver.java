package hacks;

import java.net.InetAddress;

/**
 * An interface implemented by classes that wish to be notified whenever a
 * message is sent or received by a a messenger.
 */
public interface MessageObserver {
    /**
     * This method will be called on a registered observer whenever the 
     * messenger that it is observing receives a message.
     * 
     * @param sender The address of the host that sent the message.
     * @param port The port from which the message was sent.
     * @param message The content of the message.
     */
    public void messageReceived(InetAddress sender, int port, String message);

    /**
     * This method will be called on a registered observer whenever the 
     * messenger that it is observing sends a message.
     * 
     * @param recipient The address of the host receiving the message.
     * @param port The port to which the message is being sent.
     * @param message The content of the message.
     */
    public void messageSent(InetAddress recipient, int port, String message);
}
