package com.fighting.weatherdress.weather.dto.api.subclass;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ShortTermWeatherItem {
  private String baseDate;
  private String baseTime;
  private String category;
  private String fcstDate;
  private String fcstTime;
  private String fcstValue;
  private int nx;
  private int ny;
}
