package com.HbotondS.client.Client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Client {
    private final String address;
    private final int port;
    private Socket socket = null;
    private DataInputStream input = null;
    private DataOutputStream out = null;

    public Client(String address, int port) {
        this.address = address;
        this.port = port;
    }

    public void run() {
        this.start();

        this.communicate();

        this.stop();
    }

    private void start() {
        try {
            socket = new Socket(this.address, this.port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Connected");

        input = new DataInputStream(System.in);

        try {
            out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void communicate() {
        String line = "";

        while (!line.equals("over"))
        {
            try
            {
                line = input.readLine();
                out.writeUTF(line);
            }
            catch(IOException e)
            {
                System.out.println(e.getMessage());
            }
        }
    }

    private void stop() {
        System.out.println("Disconnected");
        try
        {
            input.close();
            out.close();
            socket.close();
        }
        catch(IOException e)
        {
            System.out.println(e.getMessage());
        }
    }
}
