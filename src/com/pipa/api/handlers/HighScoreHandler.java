package com.pipa.api.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pipa.api.PlayerData;
import com.pipa.api.ScorePosition;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HighScoreHandler implements Handler {

  private static ObjectMapper objectMapper = new ObjectMapper();

  private static PlayerData playerData = new PlayerData();

  private static List<ScorePosition> scorePosition = new ArrayList<ScorePosition>();

  @Override
  public void handle(HttpExchange exchange) throws IOException {
    byte[] response;
    if ("GET".equals(exchange.getRequestMethod())) {
      List<Map.Entry<Long, Long>> results = playerData.sortMap(ScoreRegisterHandler.getPlayersScore());

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
  }
}
