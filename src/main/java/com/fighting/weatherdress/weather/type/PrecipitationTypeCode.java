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
public enum PrecipitationTypeCode {
  NONE("0", "없음"),
  RAIN("1", "비"),
  RAIN_AND_SNOW("2", "비/눈"),
  SNOW("3", "눈"),
  SHOWER("4", "소나기");

  private final String code;
  private final String precipitationType;

  private static final Map<String, PrecipitationTypeCode> codes =
      Collections.unmodifiableMap(Stream.of(values())
          .collect(Collectors.toMap(PrecipitationTypeCode::getCode, Function.identity())));

  public static PrecipitationTypeCode findByCode(String code) {
    return codes.get(code);
  }
}
