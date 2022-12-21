package com.HbotondS.client;

import com.HbotondS.client.Client.Client;

public class Main {

    public static void main(String[] args) {
        Client client = new Client("localhost", 420);

        client.run();
    }
}