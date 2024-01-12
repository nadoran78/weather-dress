package com.fighting.weatherdress.weather.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fighting.weatherdress.global.exception.CustomException;
import com.fighting.weatherdress.global.type.ErrorCode;
import com.fighting.weatherdress.weather.dto.LongTermWeatherResponse;
import java.net.URISyntaxException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class LongTermWeatherServiceTest {

  @Autowired
  private LongTermWeatherService longTermWeatherService;
  @Autowired
  private ObjectMapper objectMapper;


  @Test
  @DisplayName("중기 날씨 업데이트 및 조회 성공 테스트")
  @Transactional
  void successUpdateLongTermWeather()
      throws URISyntaxException, NoSuchFieldException, IllegalAccessException, JsonProcessingException {

    longTermWeatherService.updateLongTermWeather();
    LongTermWeatherResponse longTermWeather = longTermWeatherService.getLongTermWeather("서울",
        "구로구");

    System.out.println(objectMapper.writeValueAsString(longTermWeather));
    Assertions.assertEquals(longTermWeather.getSido(), "서울");
    Assertions.assertEquals(longTermWeather.getSigungu(), "구로구");

  }

  @Test
  @DisplayName("중기 날씨 조회 시 부적절한 지역명 입력할 경우 예외 처리")
  void getLongTermWeather_NotFoundLocation() {
    CustomException customException = Assertions.assertThrows(CustomException.class,
        () -> longTermWeatherService.getLongTermWeather("부산",
            "영등포구"));

    Assertions.assertEquals(customException.getErrorCode(), ErrorCode.NOT_FOUND_LOCATION);
  }
}