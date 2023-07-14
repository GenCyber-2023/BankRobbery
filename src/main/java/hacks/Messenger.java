package hacks;

import java.net.InetAddress;

/**
 * A simple implementation of a messenger that can be observer by a single
 * {@link MessageObserver}.
 */
public class Messenger {
    /**
     * The register observer.
     */
    private MessageObserver observer;

    /**
     * Default constructor sets the observer to null.
     */
    protected Messenger() {
        this.observer = null;
    }

    /**
     * Registers an interested {@link MessageObserver} to be notified when
     * the messenger is used to send or receive a message.
     * 
     * @param observer The obserger to register.
     */
    public void setOnMessage(MessageObserver observer) {
        this.observer = observer;
    }

    /**
     * Used to notify the registered observer that the messenger has been used 
     * to send a message.
     * 
     * @param recipient The address of the host to which the message was sent.
     * @param port The port to which the message was sent.
     * @param message The message.
     */
    protected void messageSent(InetAddress recipient, int port, 
            String message) {
        if(this.observer != null) {
            observer.messageSent(recipient, port, message);
        }
    }

    /**
     * Used to notify the registered observer that the messenger has been used 
     * to receive a message.
     * 
     * @param sender The address of the host to which the message was sent.
     * @param port The port to which the message was sent.
     * @param message The message.
     */
    protected void messageReceived(InetAddress sender, int port, 
        String message) {
        if(this.observer != null) {
            observer.messageReceived(sender, port, message);
        }
    }
}
