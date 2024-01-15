package com.fighting.weatherdress.weather.controller;

import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fighting.weatherdress.security.filter.JwtAuthenticationFilter;
import com.fighting.weatherdress.weather.dto.DailyShortWeather;
import com.fighting.weatherdress.weather.dto.LocationDto;
import com.fighting.weatherdress.weather.dto.ShortTermWeatherResponse;
import com.fighting.weatherdress.weather.service.ShortTermWeatherService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = ShortTermWeatherController.class,
    excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtAuthenticationFilter.class)})
class ShortTermWeatherControllerTest {

  @MockBean
  private ShortTermWeatherService shortTermWeatherService;
  @Autowired
  private MockMvc mockMvc;

  @Test
  @WithMockUser
  @DisplayName("위경도로 단기 날씨 정보 조회 성공 테스트")
  void successGetShortTermWeatherByLatLng() throws Exception {
    //given
    double lng = 126.51255555555555;
    double lat = 33.25235;
    LocationDto locationDto = new LocationDto("제주특별자치도", "서귀포시");
    DailyShortWeather dailyShortWeather = DailyShortWeather.builder()
        .date("20240109")
        .build();
    ShortTermWeatherResponse response = ShortTermWeatherResponse.builder()
        .sido("제주특별자치도")
        .sigungu("서귀포시")
        .today(dailyShortWeather)
        .tomorrow(dailyShortWeather)
        .dayAfterTomorrow(dailyShortWeather)
        .build();
    given(shortTermWeatherService.convertCoordinateToLocation(anyDouble(), anyDouble()))
        .willReturn(locationDto);
    given(shortTermWeatherService.getWeatherFromApi(anyString(), anyString())).willReturn(response);
    //when
    //then
    mockMvc.perform(get("/weather/short-term/lat-lng")
            .param("lat", Double.toString(lat))
            .param("lng", Double.toString(lng))
        .contentType(MediaType.APPLICATION_JSON)
        .with(SecurityMockMvcRequestPostProcessors.csrf()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.sido").value("제주특별자치도"))
        .andExpect(jsonPath("$.sigungu").value("서귀포시"))
        .andExpect(jsonPath("$.today").hasJsonPath())
        .andDo(print());
  }

  @Test
  @WithMockUser
  @DisplayName("주소(시도, 시군구)로 단기 날씨 정보 조회 성공 테스트")
  void successGetShortTermWeatherByLocation() throws Exception {
    //given
    String sido = "제주특별자치도";
    String sigungu = "서귀포시";
    DailyShortWeather dailyShortWeather = DailyShortWeather.builder()
        .date("20240109")
        .build();
    ShortTermWeatherResponse response = ShortTermWeatherResponse.builder()
        .sido("제주특별자치도")
        .sigungu("서귀포시")
        .today(dailyShortWeather)
        .tomorrow(dailyShortWeather)
        .dayAfterTomorrow(dailyShortWeather)
        .build();
    given(shortTermWeatherService.getWeatherFromApi(anyString(), anyString())).willReturn(response);
    //when
    //then
    mockMvc.perform(get("/weather/short-term/location")
            .param("sido", sido)
            .param("sigungu", sigungu)
            .contentType(MediaType.APPLICATION_JSON)
            .with(SecurityMockMvcRequestPostProcessors.csrf()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.sido").value("제주특별자치도"))
        .andExpect(jsonPath("$.sigungu").value("서귀포시"))
        .andExpect(jsonPath("$.today").hasJsonPath())
        .andDo(print());
  }

}