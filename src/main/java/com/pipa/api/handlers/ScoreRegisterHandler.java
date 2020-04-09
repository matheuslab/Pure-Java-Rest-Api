package com.pipa.api.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pipa.api.player.PlayerData;
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

      try {
        PlayerData player = objectMapper.readValue(exchange.getRequestBody(), PlayerData.class);
        createOrUpdateUserScore(player);
        exchange.sendResponseHeaders(200, 0);
      } catch (Exception e){
        exchange.sendResponseHeaders(400, 0);
      }

    } else {
      exchange.sendResponseHeaders(405, -1);
    }

    exchange.close();
  }

  private void createOrUpdateUserScore(PlayerData player) {
    if(playersScore.containsKey(player.getUserId())){
      UpdateUserScore(player);
    } else {
      createANewUser(player);
    }
  }

  private void UpdateUserScore(PlayerData player) {
    playersScore.replace(player.getUserId(), player.getPoints() + playersScore.get(player.getUserId()));
  }

  private void createANewUser(PlayerData player) {
    playersScore.put(player.getUserId(), player.getPoints());
  }
}
