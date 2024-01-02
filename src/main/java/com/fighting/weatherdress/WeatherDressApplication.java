package com.fighting.weatherdress;

import com.fighting.weatherdress.security.oauth2.config.ClientKeyConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(ClientKeyConfig.class)
public class WeatherDressApplication {

  public static void main(String[] args) {
    SpringApplication.run(WeatherDressApplication.class, args);
  }

}
