package com.HbotondS.client.Client;

import com.HbotondS.client.Promise.Promise;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Client {
    private final String address;
    private final int port;
    private Socket socket = null;
    private BufferedReader in = null;
    private DataOutputStream out = null;
    private DataInputStream inout = null;

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

        in = new BufferedReader(new InputStreamReader(System.in));

        try {
            out = new DataOutputStream(socket.getOutputStream());
            inout = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void communicate() {
        var line = "";
        while (true) {
            try {
                line = in.readLine();
                out.writeUTF(line);
                if (line.equals("over")) {
                    break;
                } else {
                    final var executor = Executors.newFixedThreadPool(2);
                    new Promise<String>()
                            .fulfillAsync(() -> inout.readUTF(), executor)
                            .thenAccept((out) -> {
                                System.out.println(out);
                                executor.shutdown();
                            });
                }
            }
            catch(IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void stop() {
        System.out.println("Disconnected");
        try
        {
            in.close();
            out.close();
            socket.close();
        }
        catch(IOException e)
        {
            System.out.println(e.getMessage());
        }
    }
}
