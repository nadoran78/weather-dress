package com.fighting.weatherdress.member.controller;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fighting.weatherdress.config.WithMockCustomUser;
import com.fighting.weatherdress.member.dto.ChangePasswordDto;
import com.fighting.weatherdress.member.service.MemberInfoService;
import com.fighting.weatherdress.security.dto.MemberInfoDto;
import com.fighting.weatherdress.security.filter.JwtAuthenticationFilter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = MemberInfoController.class,
    excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtAuthenticationFilter.class)})
class MemberInfoControllerTest {

  @MockBean
  private MemberInfoService memberInfoService;
  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private ObjectMapper objectMapper;

  @Test
  @DisplayName("비밀번호 변경 성공 테스트")
  @WithMockCustomUser
  void successChangePassword() throws Exception {
    //given
    ChangePasswordDto request = ChangePasswordDto.builder()
        .newPassword("qwe123!@#")
        .code("code")
        .build();
    //when
    mockMvc.perform(post("/member/password-change")
            .contentType(MediaType.APPLICATION_JSON)
            .with(SecurityMockMvcRequestPostProcessors.csrf())
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andDo(print());
  }

  @Test
  @DisplayName("비밀번호 변경 시 유효성 검사 실패 테스트")
  @WithMockCustomUser
  void changePassword_InvalidDto() throws Exception {
    //given
    ChangePasswordDto request = ChangePasswordDto.builder()
        .newPassword("newPassword")
        .code("code")
        .build();
    //when
    mockMvc.perform(post("/member/password-change")
            .contentType(MediaType.APPLICATION_JSON)
            .with(SecurityMockMvcRequestPostProcessors.csrf())
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.newPassword")
            .value("비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요."))
        .andDo(print());
  }

  @Test
  @DisplayName("회원정보 조회 성공 테스트")
  @WithMockCustomUser
  void successGetMemberInfo() throws Exception {
    //given경
    MemberInfoDto memberInfoDto = MemberInfoDto.builder()
        .email("userEmail")
        .nickName("nickName")
        .build();
    given(memberInfoService.getMemberInfo(anyString())).willReturn(memberInfoDto);
    //when
    mockMvc.perform(get("/member")
            .contentType(MediaType.APPLICATION_JSON)
            .with(SecurityMockMvcRequestPostProcessors.csrf()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.email").value("userEmail"))
        .andExpect(jsonPath("$.nickName").value("nickName"))
        .andDo(print());
  }

  @Test
  @DisplayName("회원탈퇴 성공 테스트")
  @WithMockCustomUser
  void successDeleteMember() throws Exception {
    //given
    //when
    mockMvc.perform(delete("/member")
            .contentType(MediaType.APPLICATION_JSON)
            .with(SecurityMockMvcRequestPostProcessors.csrf()))
        .andExpect(status().isOk())
        .andDo(print());
  }

}