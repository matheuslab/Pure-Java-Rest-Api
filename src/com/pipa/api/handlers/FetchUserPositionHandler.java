package com.pipa.api.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pipa.api.player.PlayerData;
import com.pipa.api.player.ScorePosition;
import com.sun.net.httpserver.HttpExchange;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map.Entry;

@Data
@AllArgsConstructor
@Builder
public class FetchUserPositionHandler implements Handler {

  private static PlayerData playerData = new PlayerData();

  @Override
  public void handle(HttpExchange exchange) throws IOException {

    if ("GET".equals(exchange.getRequestMethod())) {
      List<Entry<Long, Long>> orderedResultsList = playerData.sortMap(ScoreRegisterHandler.getPlayersScore());

      returningUserScoreDetails(exchange, getUserIdFromPathParam(exchange), orderedResultsList);

    } else {
      exchange.sendResponseHeaders(405, -1);
    }

    exchange.close();
  }

  private long getUserIdFromPathParam(HttpExchange exchange) {
    String path = exchange.getRequestURI().getPath();
    return Long.parseLong(path.split("/")[1]);
  }

  private void returningUserScoreDetails(
      HttpExchange exchange,
      long userId,
      List<Entry<Long, Long>> orderedResultsList
  ) throws IOException {

    byte[] response;
    ObjectMapper objectMapper = new ObjectMapper();

    response = objectMapper.writeValueAsBytes(getUserPosition(userId, orderedResultsList));
    exchange.sendResponseHeaders(200, response.length);
    OutputStream os = exchange.getResponseBody();
    os.write(response);
    os.close();
  }

  private ScorePosition getUserPosition(long userId, List<Entry<Long, Long>> results) {
    if (!ScoreRegisterHandler.getPlayersScore().containsKey(userId)) {
      return registerNewUserAtLastPosition(userId, results);
    }

    return getScoreDetailsForGivenUser(userId, results);

  }

  private ScorePosition getScoreDetailsForGivenUser(long userId, List<Entry<Long, Long>> results) {
    for (Entry<Long, Long> res : results) {
      if (res.getKey() == userId) {
        return new ScorePosition(userId, res.getValue(), results.indexOf(res) + 1);
      }
    }
    return null;
  }

  private ScorePosition registerNewUserAtLastPosition(long userId, List<Entry<Long, Long>> results) {
    ScoreRegisterHandler.setPlayersScore(userId, 0L);
    return new ScorePosition(userId, 0L, results.size()+1);
  }

}
