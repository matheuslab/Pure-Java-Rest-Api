package com.pipa.api;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class Application {

    public static void main(String[] args) throws IOException {
        int serverPort = 8000;
        HttpServer server = HttpServer.create(new InetSocketAddress(serverPort), 0);
        server.createContext("/", (exchange -> {
            if ("GET".equals(exchange.getRequestMethod())) {
                String params = exchange.getRequestURI().getPath();
                String respText = String.format("Hello %s!", params);
                exchange.sendResponseHeaders(200, respText.getBytes().length);
                OutputStream output = exchange.getResponseBody();
                output.write(respText.getBytes());
                output.flush();
            } else {
                exchange.sendResponseHeaders(405, -1); // 405 Method Not Allowed
            }
            exchange.close();
        }));
        server.setExecutor(null); // creates a default executor
        server.start();
    }
}