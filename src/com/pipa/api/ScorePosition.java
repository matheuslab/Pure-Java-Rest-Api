package com.pipa.api;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class ScorePosition extends PlayerData{

  private Integer position;

  public ScorePosition(Long userId, Long points, Integer position) {
    super(userId, points);
    this.position = position;
  }

}
