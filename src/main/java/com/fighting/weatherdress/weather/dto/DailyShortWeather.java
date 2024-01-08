package com.fighting.weatherdress.weather.dto;

import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DailyShortWeather {

  private String date;
  private Map<String, Integer> hourlyTemperature = new HashMap<>();
  private Map<String, String> hourlySkyStatus = new HashMap<>();
  private Map<String, String> hourlyPrecipitationType = new HashMap<>();
  private int maxTemperature;
  private int minTemperature;
  private int averageTemperature;
  private String averageSkyStatus;
  private String dress;

  public DailyShortWeather(String date) {
    this.date = date;
  }
}
