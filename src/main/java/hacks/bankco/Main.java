package hacks.bankco;

import java.io.IOException;

import hacks.bankco.server.BankServer;
import hacks.wiretiger.WireTigerServer;

public class Main {
    public static void main(String[] args) throws IOException {
        WireTigerServer wireTigerServer = new WireTigerServer();
        Thread wireTigerThread = new Thread(wireTigerServer);
        // wireTigerThread.setDaemon(true);
        wireTigerThread.start();

        BankServer bankServer = new BankServer();
        bankServer.setOnMessage(wireTigerServer);
        Thread bankServerThread = new Thread(bankServer);
        bankServerThread.start();
    }
}
