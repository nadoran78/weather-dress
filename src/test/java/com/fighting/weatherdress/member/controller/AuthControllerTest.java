package com.fighting.weatherdress.member.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fighting.weatherdress.config.WithMockCustomUser;
import com.fighting.weatherdress.member.dto.SignInDto;
import com.fighting.weatherdress.member.service.AuthService;
import com.fighting.weatherdress.security.dto.TokenResponse;
import com.fighting.weatherdress.security.filter.JwtAuthenticationFilter;
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

@WebMvcTest(controllers = AuthController.class,
    excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtAuthenticationFilter.class)})
class AuthControllerTest {

  @MockBean
  private AuthService authService;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  @DisplayName("로그인 성공 테스트")
  @WithMockUser
  void successSignIn() throws Exception {
    //given
    SignInDto request = SignInDto.builder()
        .email("abcd@abcd.com")
        .password("12345")
        .build();
    TokenResponse tokenResponse = TokenResponse.builder()
        .email("11")
        .accessToken("22")
        .refreshToken("33")
        .build();
    given(authService.signIn(any(SignInDto.class))).willReturn(tokenResponse);
    //when
    mockMvc.perform(post("/sign-in")
            .contentType(MediaType.APPLICATION_JSON)
            .with(SecurityMockMvcRequestPostProcessors.csrf())
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.email").value("11"))
        .andExpect(jsonPath("$.accessToken").value("22"))
        .andExpect(jsonPath("$.refreshToken").value("33"))
        .andDo(print());
  }

  @Test
  @DisplayName("로그아웃 성공 테스트")
  @WithMockCustomUser
  void successSignOut() throws Exception {
    mockMvc.perform(post("/sign-out")
            .contentType(MediaType.APPLICATION_JSON)
            .with(SecurityMockMvcRequestPostProcessors.csrf())
            .header("Authorization", "accessToken"))
        .andExpect(status().isOk())
        .andDo(print());
  }

  @Test
  @DisplayName("토큰 재발급 성공 테스트")
  @WithMockUser
  void successReissueToken() throws Exception {
    //given
    TokenResponse tokenResponse = TokenResponse.builder()
        .email("11")
        .accessToken("22")
        .refreshToken("33")
        .build();
    given(authService.reissueToken(anyString())).willReturn(tokenResponse);
    //when
    mockMvc.perform(post("/sign-in/reissue")
            .contentType(MediaType.APPLICATION_JSON)
            .with(SecurityMockMvcRequestPostProcessors.csrf())
            .header("RefreshToken", "refreshToken"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.email").value("11"))
        .andExpect(jsonPath("$.accessToken").value("22"))
        .andExpect(jsonPath("$.refreshToken").value("33"))
        .andDo(print());
  }

}