package com.HbotondS.server;

import com.HbotondS.server.Server.Server;

public class Main {
    public static void main(String[] args) {
        Server server = new Server(420);

        server.run();
    }
}