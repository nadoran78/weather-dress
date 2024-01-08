package com.fighting.weatherdress.weather.type;

import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SkyCode {
  SUNNY("1", "맑음"),
  PARTLY_CLOUDY("3", "구름많음"),
  MOSTLY_CLOUDY("4", "흐림");


  private final String code;
  private final String skyStatus;

  private static final Map<String, SkyCode> codes = Collections.unmodifiableMap(
      Stream.of(values()).collect(Collectors.toMap(SkyCode::getCode, Function.identity())));
  public static SkyCode findByCode(String code) {
    return codes.get(code);
  }
}
