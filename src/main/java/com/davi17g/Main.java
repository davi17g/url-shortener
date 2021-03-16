package com.davi17g;

import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class Main {

    private static final String hostAddress = "127.0.0.1";
    private static final int port = 1488;
    private static final int dbport = 3000;
    private static final String dbAddress = "127.0.0.1";
    private static final int idSize = 6;

    public static void main(String[] args) {
        System.out.println("Starting url-shortener");
        try {
            ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);
            HttpServer httpServer = HttpServer.create();
            httpServer.setExecutor(threadPoolExecutor);
            httpServer.bind(new InetSocketAddress(hostAddress, port), 0);

            httpServer.createContext("/", new Shortener(dbAddress, dbport, hostAddress, port, idSize));

            httpServer.start();
        } catch (IOException e) {
            System.err.println(e);

        }
    }
}
