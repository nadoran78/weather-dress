package com.fighting.weatherdress.weather.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fighting.weatherdress.weather.dto.LocationDto;
import com.fighting.weatherdress.weather.dto.ShortTermWeatherResponse;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Disabled
@SpringBootTest
class ShortTermWeatherServiceTest {

  @Autowired
  private ShortTermWeatherService shortTermWeatherService;
  @Autowired
  private ObjectMapper objectMapper;

  @Test
  @DisplayName("단기 날씨 정보 조회 서비스 성공 테스트")
  void successGetWeatherFromApi() throws URISyntaxException, JsonProcessingException {
    //given
    double lat = 37.49265;
    double lng = 126.88959722222224;
    LocationDto locationDto = shortTermWeatherService.convertCoordinateToLocation(lat, lng);
    //when
    ShortTermWeatherResponse weatherFromApi = shortTermWeatherService.getWeatherFromApi(
        locationDto.getSido(), locationDto.getSigungu());
    //then
    LocalDate now = LocalDate.now();

    System.out.println(objectMapper.writeValueAsString(weatherFromApi));
    // 지역 검증
    Assertions.assertEquals(weatherFromApi.getSido(), "서울");
    Assertions.assertEquals(weatherFromApi.getSigungu(), "구로구");
    // 날짜 검증
    Assertions.assertEquals(weatherFromApi.getToday().getDate(),
        now.format(DateTimeFormatter.ofPattern("yyyyMMdd")));
    Assertions.assertEquals(weatherFromApi.getTomorrow().getDate(),
        now.plusDays(1).format(DateTimeFormatter.ofPattern("yyyyMMdd")));
    Assertions.assertEquals(weatherFromApi.getDayAfterTomorrow().getDate(),
        now.plusDays(2).format(DateTimeFormatter.ofPattern("yyyyMMdd")));
  }

}