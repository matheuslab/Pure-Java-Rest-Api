package com.pipa.api.utils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Utils {

  public List<Map.Entry<Long, Long>> sortMap (Map<Long, Long> map){
    return map
        .entrySet()
        .stream()
        .sorted(Map.Entry.comparingByValue(Collections.reverseOrder()))
        .collect(Collectors.toList());
  }

}
