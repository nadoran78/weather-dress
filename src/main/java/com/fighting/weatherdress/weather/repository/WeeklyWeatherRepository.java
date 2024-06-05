package com.fighting.weatherdress.weather.repository;

import com.fighting.weatherdress.global.entity.Location;
import com.fighting.weatherdress.weather.entity.WeeklyWeather;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WeeklyWeatherRepository extends JpaRepository<WeeklyWeather, Long> {
  void deleteAllByCreatedAtBefore(LocalDateTime date);
  List<WeeklyWeather> findAllByLocation(Location location);
}
