package hacks;

import java.net.InetAddress;

public class Messenger {
    private MessageObserver observer;

    public void setOnMessage(MessageObserver observer) {
        this.observer = observer;
    }

    protected void messageSent(InetAddress recipient, int port, 
            String message) {
        if(this.observer != null) {
            observer.messageSent(recipient, port, message);
        }
    }

    protected void messageReceived(InetAddress sender, int port, 
        String message) {
        if(this.observer != null) {
            observer.messageReceived(sender, port, message);
        }
    }
}
