package com.fighting.weatherdress;

import com.fighting.weatherdress.s3.config.properties.S3Properties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WeatherDressApplication {

  public static void main(String[] args) {
    SpringApplication.run(WeatherDressApplication.class, args);
  }

}
