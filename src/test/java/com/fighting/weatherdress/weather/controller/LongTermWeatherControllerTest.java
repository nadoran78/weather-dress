package com.fighting.weatherdress.weather.controller;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fighting.weatherdress.security.filter.JwtAuthenticationFilter;
import com.fighting.weatherdress.weather.dto.LongTermWeatherResponse;
import com.fighting.weatherdress.weather.service.LongTermWeatherService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = LongTermWeatherController.class,
    excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtAuthenticationFilter.class)})
class LongTermWeatherControllerTest {

  @MockBean
  private LongTermWeatherService longTermWeatherService;
  @Autowired
  private MockMvc mockMvc;

  @Test
  @DisplayName("중기 날씨 예보 조회 성공 테스트")
  @WithMockUser
  void success_getLongTermWeather() throws Exception {
    //given
    LongTermWeatherResponse response = LongTermWeatherResponse.builder()
        .sido("서울")
        .sigungu("구로구")
        .build();
    //when
    given(longTermWeatherService.getLongTermWeather(anyString(), anyString())).willReturn(response);
    //then
    mockMvc.perform(get("/weather/long-term")
            .param("sido", "서울")
            .param("sigungu", "구로구"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.sido").value("서울"))
        .andExpect(jsonPath("$.sigungu").value("구로구"))
        .andDo(print());
  }

}