package com.fighting.weatherdress.weather.controller;

import com.fighting.weatherdress.weather.dto.LocationDto;
import com.fighting.weatherdress.weather.dto.ShortTermWeatherResponse;
import com.fighting.weatherdress.weather.service.ShortTermWeatherService;
import java.net.URISyntaxException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/weather/short-term")
public class ShortTermWeatherController {

  private final ShortTermWeatherService shortTermWeatherService;

  // 위도 경도로 단기 날씨 조회
  @GetMapping("/lat-lng")
  public ResponseEntity<ShortTermWeatherResponse> getShortTermWeatherByLatLng(
      @RequestParam double lat,
      @RequestParam double lng) throws URISyntaxException {
    LocationDto locationDto = shortTermWeatherService.convertCoordinateToLocation(lat, lng);
    ShortTermWeatherResponse weatherFromApi = shortTermWeatherService.getWeatherFromApi(
        locationDto.getSido(), locationDto.getSigungu());
    return ResponseEntity.ok(weatherFromApi);
  }

  // 시도, 시군구로 단기 날씨 조회
  @GetMapping("/location")
  public ResponseEntity<ShortTermWeatherResponse> getShortTermWeatherByLocation(
      @RequestParam String sido,
      @RequestParam String sigungu) throws URISyntaxException {
    ShortTermWeatherResponse weatherFromApi = shortTermWeatherService.getWeatherFromApi(
        sido, sigungu);
    return ResponseEntity.ok(weatherFromApi);
  }

}
