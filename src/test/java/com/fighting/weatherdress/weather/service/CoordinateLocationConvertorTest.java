package com.fighting.weatherdress.weather.service;

import com.fighting.weatherdress.weather.dto.LocationDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CoordinateLocationConvertorTest {

  @Autowired
  private CoordinateLocationConvertor coordinateLocationConvertor;

  @Test
  @DisplayName("위경도를 주소(시도, 시군구)로 변환 성공 테스트")
  void successConvert() {
    //given
    double lng = 126.51255555555555;
    double lat = 33.25235;
    //when
    LocationDto locationDto = coordinateLocationConvertor.convertCoordinateToLocation(lat, lng);
    //then
    Assertions.assertEquals(locationDto.getSido(), "제주특별자치도");
    Assertions.assertEquals(locationDto.getSigungu(), "서귀포시");
  }
}