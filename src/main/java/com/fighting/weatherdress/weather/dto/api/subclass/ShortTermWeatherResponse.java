package com.fighting.weatherdress.weather.dto.api.subclass;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ShortTermWeatherResponse {
  private Header header;
  private ShortTermWeatherBody body;
}
