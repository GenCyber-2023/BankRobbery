package hacks;

import java.net.InetAddress;

public interface MessageObserver {
    public void messageReceived(InetAddress sender, int port, String message);

    public void messageSent(InetAddress recipient, int port, String message);
}
