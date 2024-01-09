package com.fighting.weatherdress.weather.dto;

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
public class ShortTermWeatherResponse {
  private String sido;
  private String sigungu;
  private DailyShortWeather today;
  private DailyShortWeather tomorrow;
  private DailyShortWeather dayAfterTomorrow;
}
