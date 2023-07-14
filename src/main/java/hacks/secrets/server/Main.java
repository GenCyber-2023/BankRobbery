package hacks.secrets.server;

import java.io.IOException;

import hacks.wiretiger.WireTigerServer;

public class Main {
    public static void main(String[] args) throws IOException {
        WireTigerServer wireTigerServer = new WireTigerServer();
        Thread wireTigerThread = new Thread(wireTigerServer);
        wireTigerThread.start();

        SecretServer server = new SecretServer();
        server.setOnMessage(wireTigerServer);
        Thread serverThread = new Thread(server);
        serverThread.start();
    }
}
