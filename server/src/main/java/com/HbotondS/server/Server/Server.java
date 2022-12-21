package com.HbotondS.server.Server;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private final int port;
    private Socket socket = null;
    private ServerSocket server = null;
    private DataInputStream in = null;

    public Server(int port) {
        this.port = port;
    }

    public void run() {
        this.start();

        this.communicate();

        this.stop();
    }

    private void start() {
        try {
            this.server = new ServerSocket(port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Server started");
        System.out.println("Waiting for a client ...");
    }

    private void communicate() {
        try {
            socket = server.accept();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Client accepted");

        try {
            in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String line = "";

        while (!line.equals("over")) {
            try {
                line = in.readUTF();
                System.out.println(line);
            }
            catch(IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void stop() {
        System.out.println("Closing connection");

        try {
            socket.close();
            in.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}