package com.fighting.weatherdress.weather.dto;

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
public class LongTermWeatherResponse {

  private String sido;
  private String sigungu;
  private List<WeeklyWeatherDto> weeklyWeathers;

}
