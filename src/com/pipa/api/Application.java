package com.pipa.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Application {

    private static ObjectMapper objectMapper = new ObjectMapper();
    private static Map<Long, Long> playersScore = new HashMap<Long, Long>();
    private static PlayerData playerData = new PlayerData();
    private static List<ScorePosition> scorePosition = new ArrayList<ScorePosition>();

    public static void main(String[] args) throws IOException {
        int serverPort = 8000;
        HttpServer server = HttpServer.create(new InetSocketAddress(serverPort), 0);

        //GET Score of a user
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

        //GET HighScore List
        server.createContext("/highscorelist", (exchange -> {
            byte[] response;
            if ("GET".equals(exchange.getRequestMethod())) {
                List<Entry<Long, Long>> results = playerData.sortMap(playersScore);

                scorePosition.clear();

                results.stream().limit(20).forEach(res -> {
                    scorePosition.add(new ScorePosition(res.getKey(), res.getValue(), results.indexOf(res)+1));
                });

                response = objectMapper.writeValueAsBytes(scorePosition);
                exchange.sendResponseHeaders(200, response.length);
                OutputStream os = exchange.getResponseBody();
                os.write(response);
                os.close();

            } else {
                exchange.sendResponseHeaders(405, -1); // 405 Method Not Allowed
            }

            exchange.close();
        }));

        //POST
        server.createContext("/post", (exchange -> {
            if ("POST".equals(exchange.getRequestMethod())) {
                PlayerData player = objectMapper.readValue(exchange.getRequestBody(), PlayerData.class);

                if(playersScore.containsKey(player.getUserId())){
                    playersScore.replace(player.getUserId(), player.getPoints() + playersScore.get(player.getUserId()));
                } else {
                    playersScore.put(player.getUserId(), player.getPoints());
                }
                exchange.sendResponseHeaders(200, 0);
            } else {
                exchange.sendResponseHeaders(405, -1); // 405 Method Not Allowed
            }

            exchange.close();
        }));

        server.setExecutor(null); // creates a default executor
        server.start();
    }
}