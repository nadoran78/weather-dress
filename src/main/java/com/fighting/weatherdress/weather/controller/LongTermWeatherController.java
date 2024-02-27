package com.fighting.weatherdress.weather.controller;

import com.fighting.weatherdress.weather.dto.LongTermWeatherResponse;
import com.fighting.weatherdress.weather.service.LongTermWeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/weather/long-term")
public class LongTermWeatherController {

  private final LongTermWeatherService longTermWeatherService;

  @GetMapping
  public ResponseEntity<LongTermWeatherResponse> getLongTermWeather(@RequestParam String sido,
      @RequestParam String sigungu) {
    LongTermWeatherResponse longTermWeather = longTermWeatherService.getLongTermWeather(sido,
        sigungu);
    return ResponseEntity.ok(longTermWeather);
  }

}
