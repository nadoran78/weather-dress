package com.fighting.weatherdress.weather.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "long.weather")
@Component
@Getter
@Setter
public class LongTermWeatherProperties {
  private String serviceKey;
  private int numOfRows;
  private String dataType;
}
