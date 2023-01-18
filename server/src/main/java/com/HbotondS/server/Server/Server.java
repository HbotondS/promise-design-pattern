package com.HbotondS.server.Server;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private final int port;
    private Socket socket = null;
    private ServerSocket server = null;
    private DataInputStream in = null;
    private DataOutputStream out = null;

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
            out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        var line = "";
        while (true) {
            try {
                line = in.readUTF();
                if (line.equals("over")) {
                    break;
                } else {
                    String finalLine = line;
                    new Thread(() -> {
                        var sleepTime = Math.floor(Math.random() * 10000);
                        System.out.println(finalLine
                                .concat(" waits: ")
                                .concat(Double.toString(sleepTime))
                        );
                        try {
                            Thread.sleep((long) sleepTime);
                            out.writeUTF("".concat("out: ").concat(finalLine));
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }
                    }).start();
                }
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