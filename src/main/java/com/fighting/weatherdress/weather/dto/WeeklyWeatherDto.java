package com.fighting.weatherdress.weather.dto;

import com.fighting.weatherdress.weather.entity.WeeklyWeather;
import java.time.LocalDate;
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
public class WeeklyWeatherDto {
  private LocalDate date;
  private int minTemperature;
  private int maxTemperature;
  private String morningWeatherForecast;
  private String afternoonWeatherForecast;

  public static WeeklyWeatherDto fromEntity(WeeklyWeather weeklyWeather) {
    return WeeklyWeatherDto.builder()
        .date(weeklyWeather.getDate())
        .minTemperature(weeklyWeather.getMinTemperature())
        .maxTemperature(weeklyWeather.getMaxTemperature())
        .morningWeatherForecast(weeklyWeather.getMorningWeatherForecast())
        .afternoonWeatherForecast(weeklyWeather.getAfternoonWeatherForecast())
        .build();
  }
}
