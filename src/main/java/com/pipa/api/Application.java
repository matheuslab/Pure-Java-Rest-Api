package com.pipa.api;

import com.pipa.api.handlers.FetchUserPositionHandler;
import com.pipa.api.handlers.HighScoreHandler;
import com.pipa.api.handlers.ScoreRegisterHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Application {

    public static void main(String[] args) throws IOException {
        int serverPort = 8000;
        HttpServer server = HttpServer.create(new InetSocketAddress(serverPort), 0);

        server.createContext("/", new FetchUserPositionHandler());

        server.createContext("/highscorelist", new HighScoreHandler());

        server.createContext("/score", new ScoreRegisterHandler());

        server.setExecutor(null);
        server.start();
    }
}
