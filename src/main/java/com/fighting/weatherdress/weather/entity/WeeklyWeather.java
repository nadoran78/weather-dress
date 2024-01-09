package com.fighting.weatherdress.weather.entity;

import com.fighting.weatherdress.global.entity.Location;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Entity
public class WeeklyWeather {

  @Id
  private Long id;

  @Column(nullable = false)
  private LocalDate date;

  @Column(nullable = false)
  private int minTemperature;

  @Column(nullable = false)
  private int maxTemperature;

  @Column(nullable = false)
  private String morningWeatherForecast;

  @Column(nullable = false)
  private String afternoonWeatherForecast;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "location_id")
  private Location location;

}
