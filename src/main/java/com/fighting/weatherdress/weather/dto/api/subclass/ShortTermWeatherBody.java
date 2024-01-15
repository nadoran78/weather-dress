package com.fighting.weatherdress.weather.dto.api.subclass;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ShortTermWeatherBody {
  private String dataType;
  private ShortTermWeatherItems items;
  private int pageNo;
  private int numOfRows;
  private int totalCount;
}
