package com.fighting.weatherdress.weather.dto.api.subclass;

import java.util.List;
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
public class WeeklyTemperatureItems {
  private List<WeeklyTemperatureItem> item;
}
