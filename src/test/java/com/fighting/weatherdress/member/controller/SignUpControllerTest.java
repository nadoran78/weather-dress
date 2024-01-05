package com.fighting.weatherdress.member.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fighting.weatherdress.config.WithMockCustomUser;
import com.fighting.weatherdress.member.dto.SignUpDto;
import com.fighting.weatherdress.member.dto.VerifyEmailDto;
import com.fighting.weatherdress.member.service.SignUpService;
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

@WebMvcTest(controllers = SignUpController.class,
    excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtAuthenticationFilter.class)})
class SignUpControllerTest {

  @MockBean
  private SignUpService signUpService;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @DisplayName("회원가입 요청 성공")
  @Test
  @WithMockUser
  void successSignUp() throws Exception {
    //given
    SignUpDto request = SignUpDto.builder()
        .email("abcd@abcd.com")
        .password("qwe123!@#")
        .nickName("홍길동")
        .build();

    //when
    mockMvc.perform(post("/sign-up")
            .contentType(MediaType.APPLICATION_JSON)
            .with(SecurityMockMvcRequestPostProcessors.csrf())
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andDo(print());
  }

  @DisplayName("회원가입 시 유효성 검사 실패 테스트")
  @Test
  @WithMockUser
  void signUp_InvalidDto() throws Exception {
    //given
    SignUpDto request = SignUpDto.builder()
        .email("abcdabcd.com")
        .password("qwe123")
        .nickName("123")
        .build();

    //when
    mockMvc.perform(post("/sign-up")
            .contentType(MediaType.APPLICATION_JSON)
            .with(SecurityMockMvcRequestPostProcessors.csrf())
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.password")
            .value("비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요."))
        .andExpect(jsonPath("$.nickName")
            .value("이름은 숫자, 특수문자를 제외한 2~10자리여야 합니다."))
        .andExpect(jsonPath("$.email")
            .value("유효한 이메일 주소가 아닙니다."))
        .andDo(print());
  }

  @DisplayName("이메일 인증 성공")
  @Test
  @WithMockUser
  void successVerifyEmail() throws Exception {
    //given
    VerifyEmailDto request = VerifyEmailDto.builder()
        .email("abcd@abcd.com")
        .code("aaaaaa")
        .build();

    //when
    mockMvc.perform(post("/verify")
            .contentType(MediaType.APPLICATION_JSON)
            .with(SecurityMockMvcRequestPostProcessors.csrf())
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andDo(print());
  }

  @Test
  @WithMockCustomUser
  @DisplayName("이메일 발송 성공 테스트")
  void successSendEmailWithVerifyCode() throws Exception {
    //then
    mockMvc.perform(post("/email")
            .contentType(MediaType.APPLICATION_JSON)
            .with(SecurityMockMvcRequestPostProcessors.csrf())
            .content(objectMapper.writeValueAsString("email")))
        .andExpect(status().isOk())
        .andDo(print());
  }
}