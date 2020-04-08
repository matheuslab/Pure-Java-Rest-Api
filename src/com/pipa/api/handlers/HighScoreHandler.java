package com.pipa.api.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pipa.api.player.PlayerData;
import com.pipa.api.player.ScorePosition;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HighScoreHandler implements Handler {

  private static PlayerData playerData = new PlayerData();

  @Override
  public void handle(HttpExchange exchange) throws IOException {
    if ("GET".equals(exchange.getRequestMethod())) {
      List<Map.Entry<Long, Long>> results = playerData.sortMap(ScoreRegisterHandler.getPlayersScore());

      returningHighScoreList(exchange, results);

    } else {
      exchange.sendResponseHeaders(405, -1);
    }

    exchange.close();
  }

  private void returningHighScoreList(HttpExchange exchange, List<Map.Entry<Long, Long>> results) throws IOException {
    byte[] response;
    ObjectMapper objectMapper = new ObjectMapper();

    response = objectMapper.writeValueAsBytes(getHighScoreList(results));
    exchange.sendResponseHeaders(200, response.length);
    OutputStream os = exchange.getResponseBody();
    os.write(response);
    os.close();
  }

  private List<ScorePosition> getHighScoreList(List<Map.Entry<Long, Long>> results) {
    List<ScorePosition> scorePosition = new ArrayList<ScorePosition>();

    results.stream().limit(20).forEach(res -> {
      scorePosition.add(new ScorePosition(res.getKey(), res.getValue(), results.indexOf(res)+1));
    });

    return scorePosition;
  }

}
