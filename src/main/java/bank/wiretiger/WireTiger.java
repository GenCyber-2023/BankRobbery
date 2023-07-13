package bank.wiretiger;

import java.net.InetAddress;

import bank.MessageObserver;

public class WireTiger implements MessageObserver {

    @Override
    public void messageReceived(InetAddress sender, int port, String message) {

    }

    @Override
    public void messageSent(InetAddress recipient, int port, String message) {
 
    }
    
}
