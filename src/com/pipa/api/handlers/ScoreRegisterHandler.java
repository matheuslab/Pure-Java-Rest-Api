package com.pipa.api.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pipa.api.PlayerData;
import com.sun.net.httpserver.HttpExchange;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@Builder
public class ScoreRegisterHandler implements Handler {

  private static ObjectMapper objectMapper = new ObjectMapper();
  private static Map<Long, Long> playersScore = new HashMap<Long, Long>();

  public static Map<Long, Long> getPlayersScore() {
    return playersScore;
  }

  public static void setPlayersScore(Long key, Long value) {
    ScoreRegisterHandler.playersScore.put(key, value);
  }

  @Override
  public void handle(HttpExchange exchange) throws IOException {
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
  }
}
