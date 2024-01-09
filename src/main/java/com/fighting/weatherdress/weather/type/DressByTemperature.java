package com.fighting.weatherdress.weather.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DressByTemperature {
  SO_HOT(28, 100, "민소매, 반팔, 반바지, 치마"),
  HOT(23, 27, "반팔, 얇은 셔츠, 반바지, 면바지"),
  SO_WARM(20, 22, "얇은 가디건, 긴팔티, 면바지, 청바지"),
  WARM(17, 19, "얇은 니트, 가디건, 맨투맨, 얇은 자켓, 면바지, 청바지"),
  COOL(12, 16, "자켓, 가디건, 야상, 맨투맨, 니트, 스타킹, 청바지, 면바지"),
  SO_COOL(9, 11, "자켓, 트렌치코트, 야상, 니트, 스타킹, 청바지, 면바지"),
  COLD(5, 8, "코트, 히트텍, 니트, 청바지, 레깅스"),
  SO_COLD(-100, 4, "패딩, 투꺼운코트, 목도리, 기모제품");

  private final int minTemperature;
  private final int maxTemperature;
  private final String dress;

  public static String findDressByTemperature(int temperature) {
    DressByTemperature[] values = values();
    for (DressByTemperature value : values) {
      if (temperature <= value.maxTemperature && temperature >= value.minTemperature) {
        return value.getDress();
      }
    }
    return null;
  }
}
