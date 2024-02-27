package com.fighting.weatherdress.weather.repository;

import com.fighting.weatherdress.weather.entity.WeeklyWeather;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WeeklyWeatherRepository extends JpaRepository<WeeklyWeather, Long> {

}
