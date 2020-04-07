package com.pipa.api;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

/*
  Jackson Builder method is different from lombok builder method, so we need
  to create a special builder on this class in order to fix this.
*/
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonDeserialize(builder = PlayerData.PlayerDataBuilder.class)
@Builder(builderClassName = "PlayerDataBuilder", toBuilder = true)
public class PlayerData{

  protected Long userId;

  protected Long points;

  public Long getUserId() {
    return userId;
  }

  public Long getPoints() {
    return points;
  }

  @JsonPOJOBuilder(withPrefix = "")
  public static class PlayerDataBuilder {

  }

  public List<Entry<Long, Long>> sortMap (Map<Long, Long> map){
    return map
        .entrySet()
        .stream()
        .sorted(Map.Entry.comparingByValue(Collections.reverseOrder()))
        .collect(Collectors.toList());
  }

}
