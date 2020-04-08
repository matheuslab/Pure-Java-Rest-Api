package com.pipa.api.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pipa.api.PlayerData;
import com.pipa.api.ScorePosition;
import com.sun.net.httpserver.HttpExchange;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@Builder
public class FetchUserPositionHandler implements Handler {

  private static ObjectMapper objectMapper = new ObjectMapper();

  private static PlayerData playerData = new PlayerData();

  private static List<ScorePosition> scorePosition = new ArrayList<ScorePosition>();

  @Override
  public void handle(HttpExchange exchange) throws IOException {
    byte[] response;
    if ("GET".equals(exchange.getRequestMethod())) {
      String params = exchange.getRequestURI().getPath();
      long userId = Long.parseLong(params.split("/")[1]);
      List<Map.Entry<Long, Long>> results = playerData.sortMap(ScoreRegisterHandler.getPlayersScore());
      scorePosition.clear();

      if (!ScoreRegisterHandler.getPlayersScore().containsKey(userId)) {
        scorePosition.add(new ScorePosition(userId, 0L, results.size()+1));
        ScoreRegisterHandler.setPlayersScore(userId, 0L);
      } else {
        results.forEach(res -> {
          if(res.getKey() == userId){
            scorePosition.add(new ScorePosition(userId, res.getValue(), results.indexOf(res)+1));
          }
        });
      }

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
