package com.fighting.weatherdress.weather.dto.api;

import com.fighting.weatherdress.weather.dto.api.subclass.WeeklyForecastResponse;
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
public class ResponseFromWeeklyForecastApi {
  private WeeklyForecastResponse response;
}
