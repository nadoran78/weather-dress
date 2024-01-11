package com.fighting.weatherdress.weather.dto.api.subclass;

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
public class WeeklyForecastBody {
  private String dataType;
  private WeeklyForecastItems items;
  private int pageNo;
  private int numOfRows;
  private int totalCount;
}
