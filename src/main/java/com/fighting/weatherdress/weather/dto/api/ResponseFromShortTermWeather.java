package com.fighting.weatherdress.weather.dto.api;

import com.fighting.weatherdress.weather.dto.api.subclass.ShortTermWeatherResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseFromShortTermWeather {
  private ShortTermWeatherResponse response;
}
