package com.fighting.weatherdress.weather.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "short.weather")
@Component
@Getter
@Setter
public class ShortTermWeatherProperties {
  private String serviceKey;
  private int numOfRows;
  private String dataType;
}
