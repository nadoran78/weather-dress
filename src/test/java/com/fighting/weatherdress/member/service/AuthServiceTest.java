package com.fighting.weatherdress.member.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.fighting.weatherdress.global.exception.CustomException;
import com.fighting.weatherdress.global.type.Authority;
import com.fighting.weatherdress.global.type.ErrorCode;
import com.fighting.weatherdress.member.domain.Member;
import com.fighting.weatherdress.member.dto.SignInDto;
import com.fighting.weatherdress.member.repository.MemberRepository;
import com.fighting.weatherdress.security.dto.TokenResponse;
import com.fighting.weatherdress.security.token.TokenProvider;
import java.util.Collections;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

  @Mock
  private MemberRepository memberRepository;
  @Mock
  private PasswordEncoder passwordEncoder;
  @Mock
  private TokenProvider tokenProvider;
  @InjectMocks
  private AuthService authService;

  @Test
  @DisplayName("로그인 성공 테스트")
  void successSignIn() {
    //given
    Member member = Member.builder()
        .email("email")
        .password("password")
        .nickName("nickname")
        .verified(true)
        .roles(Collections.singletonList(Authority.ROLE_USER.toString()))
        .build();
    TokenResponse tokenResponse = TokenResponse.builder()
        .email("11")
        .accessToken("22")
        .refreshToken("33")
        .build();
    given(memberRepository.findByEmail(anyString())).willReturn(Optional.of(member));
    given(passwordEncoder.matches(anyString(), anyString())).willReturn(true);
    given(tokenProvider.generateTokenResponse(member.getEmail(), member.getRoles()))
        .willReturn(tokenResponse);
    //when
    SignInDto request = SignInDto.builder()
        .email("abc")
        .password("111")
        .build();
    TokenResponse response = authService.signIn(request);
    //then
    assertEquals(response.getEmail(), "11");
    assertEquals(response.getAccessToken(), "22");
    assertEquals(response.getRefreshToken(), "33");
  }

  @Test
  @DisplayName("로그인 시 db에 저장된 이메일 없을 경우 예외처리")
  void signIn_NotFoundEmail() {
    //given
    given(memberRepository.findByEmail(anyString())).willReturn(Optional.empty());
    //when
    SignInDto request = SignInDto.builder()
        .email("abc")
        .password("111")
        .build();
    CustomException customException = assertThrows(CustomException.class,
        () -> authService.signIn(request));
    //then
    assertEquals(customException.getErrorCode(), ErrorCode.NOT_FOUND_EMAIL);
  }

  @Test
  @DisplayName("로그인 시 소셜로그인으로 db에 저장된 사용자일 경우 예외 처리")
  void signIn_RegisteredBySocial() {
    //given
    Member member = Member.builder()
        .email("email")
        .password(null)
        .nickName("nickname")
        .verified(true)
        .roles(Collections.singletonList(Authority.ROLE_USER.toString()))
        .build();
    given(memberRepository.findByEmail(anyString())).willReturn(Optional.of(member));
    //when
    SignInDto request = SignInDto.builder()
        .email("abc")
        .password("111")
        .build();
    CustomException customException = assertThrows(CustomException.class,
        () -> authService.signIn(request));
    //then
    assertEquals(customException.getErrorCode(), ErrorCode.REGISTERED_BY_SOCIAL);
  }

  @Test
  @DisplayName("로그인 시 비밀번호 불일치 할 경우 예외 처리")
  void signIn_IncorrectPassword() {
    //given
    Member member = Member.builder()
        .email("email")
        .password("password")
        .nickName("nickname")
        .verified(true)
        .roles(Collections.singletonList(Authority.ROLE_USER.toString()))
        .build();
    given(memberRepository.findByEmail(anyString())).willReturn(Optional.of(member));
    given(passwordEncoder.matches(anyString(), anyString())).willReturn(false);
    //when
    SignInDto request = SignInDto.builder()
        .email("abc")
        .password("111")
        .build();
    CustomException customException = assertThrows(CustomException.class,
        () -> authService.signIn(request));
    //then
    assertEquals(customException.getErrorCode(), ErrorCode.INCORRECT_PASSWORD);
  }

  @Test
  @DisplayName("로그인 시 이메일 인증되지 않은 사용자일 경우 예외 처리")
  void signIn_NeedToVerifyEmail() {
    //given
    Member member = Member.builder()
        .email("email")
        .password("password")
        .nickName("nickname")
        .verified(false)
        .roles(Collections.singletonList(Authority.ROLE_USER.toString()))
        .build();
    given(memberRepository.findByEmail(anyString())).willReturn(Optional.of(member));
    given(passwordEncoder.matches(anyString(), anyString())).willReturn(true);
    //when
    SignInDto request = SignInDto.builder()
        .email("abc")
        .password("111")
        .build();
    CustomException customException = assertThrows(CustomException.class,
        () -> authService.signIn(request));
    //then
    assertEquals(customException.getErrorCode(), ErrorCode.NEED_TO_VERIFY_EMAIL);
  }

  @Test
  @DisplayName("로그아웃 성공 테스트")
  void successSignOut() {
    //given
    given(tokenProvider.resolveTokenFromRequest(anyString())).willReturn("accessToken");
    //when
    authService.signOut("Bearer accessToken", "email");

    ArgumentCaptor<String> captor1 = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<String> captor2 = ArgumentCaptor.forClass(String.class);
    //then
    verify(tokenProvider, times(1)).addBlackList(captor1.capture(), captor2.capture());
    String token = captor1.getValue();
    String email = captor2.getValue();
    assertEquals(token, "accessToken");
    assertEquals(email, "email");
  }

  @Test
  @DisplayName("토큰 재발급 성공 테스트")
  void successReissueToken() {
    //given
    //when
    authService.reissueToken("refreshToken");
    ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
    //then
    verify(tokenProvider, times(1)).regenerateAccessToken(captor.capture());
    assertEquals(captor.getValue(), "refreshToken");
  }
}