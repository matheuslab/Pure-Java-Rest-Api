package com.pipa.api;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Data;

/*
  Jackson Builder method is different from lombok builder method, so we need
  to create a special builder on this class in order to fix this.
*/
@Data
@JsonDeserialize(builder = PlayerData.PlayerDataBuilder.class)
@Builder(builderClassName = "PlayerDataBuilder", toBuilder = true)
public class PlayerData {

  private Long userId;

  private Long points;

  @JsonPOJOBuilder(withPrefix = "")
  public static class PlayerDataBuilder {

  }

}
