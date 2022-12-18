package com.HbotondS.client;

import com.HbotondS.client.Promise.Promise;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    public static void main(String[] args) {
        final ExecutorService executor = Executors.newFixedThreadPool(2);

        var promise = new Promise<String>()
                .fulfillAsync(() -> {
                    Thread.sleep(10);
                    return "Hello world";
                }, executor);
        promise.thenAccept(System.out::println);

        executor.shutdown();
    }
}