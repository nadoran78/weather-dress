package com.fighting.weatherdress.weather.dto.api.subclass;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ShortTermWeatherItems {
  private List<ShortTermWeatherItem> item;
}
