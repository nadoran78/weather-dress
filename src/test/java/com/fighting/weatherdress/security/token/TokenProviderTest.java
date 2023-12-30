package com.fighting.weatherdress.security.token;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import com.fighting.weatherdress.global.exception.CustomException;
import com.fighting.weatherdress.global.type.Authority;
import com.fighting.weatherdress.global.type.ErrorCode;
import com.fighting.weatherdress.member.domain.Member;
import com.fighting.weatherdress.member.service.MemberService;
import com.fighting.weatherdress.security.dto.CustomUserDetails;
import com.fighting.weatherdress.security.dto.TokenResponse;
import com.fighting.weatherdress.security.token.entity.RefreshToken;
import com.fighting.weatherdress.security.token.repository.RefreshTokenRedisRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class TokenProviderTest {

  @Mock
  private MemberService memberService;
  @Mock
  private RefreshTokenRedisRepository refreshTokenRedisRepository;
  @InjectMocks
  private TokenProvider tokenProvider;

  @BeforeEach
  void tokenProviderInit() {
    ReflectionTestUtils.setField(tokenProvider, "secretKey", "weather");
    tokenProvider.init();
  }

  @Test
  @DisplayName("TokenResponse 생성 성공 테스트")
  void successGenerateTokenResponse() {
    //given
    String email = "abcd@abcd.com";
    List<String> roles = Collections.singletonList(Authority.ROLE_USER.toString());
    //when
    TokenResponse tokenResponse = tokenProvider.generateTokenResponse(email, roles);
    //then
    assertEquals(tokenResponse.getEmail(), email);
    assertNotNull(tokenResponse.getAccessToken());
    assertNotNull(tokenResponse.getRefreshToken());
  }

  @Test
  @DisplayName("AccessToken 재발행 성공 테스트")
  void successRegenerateAccessToken() {
    // given
    String email = "abcd@abcd.com";
    List<String> roles = Collections.singletonList(Authority.ROLE_USER.toString());
    TokenResponse tokenResponse = tokenProvider.generateTokenResponse(email, roles);
    RefreshToken refreshToken = RefreshToken.builder()
        .id(email)
        .token(tokenResponse.getRefreshToken())
        .build();

    given(refreshTokenRedisRepository.findById(anyString())).willReturn(Optional.of(refreshToken));
    // when
    TokenResponse result = tokenProvider.regenerateAccessToken(tokenResponse.getRefreshToken());
    // then
    assertEquals(result.getEmail(), email);
    assertNotNull(result.getAccessToken());
    assertEquals(result.getRefreshToken(), tokenResponse.getRefreshToken());
  }

  @Test
  @DisplayName("AccessToken 재발행 시 토큰이 null일 경우 예외 처리")
  void regenerateAccessToken_InvalidToken() {
    // when
    CustomException customException = assertThrows(CustomException.class,
        () -> tokenProvider.regenerateAccessToken(null));
    // then
    assertEquals(customException.getErrorCode(), ErrorCode.INVALID_TOKEN);
  }

  @Test
  @DisplayName("AccessToken 재발행 시 기존에 저장된 refreshToken이 없을 경우 예외 처리")
  void regenerateAccessToken_NotFoundRefreshToken() {
    //given
    String email = "abcd@abcd.com";
    List<String> roles = Collections.singletonList(Authority.ROLE_USER.toString());
    TokenResponse tokenResponse = tokenProvider.generateTokenResponse(email, roles);

    given(refreshTokenRedisRepository.findById(anyString())).willReturn(Optional.empty());
    //when
    CustomException customException = assertThrows(CustomException.class,
        () -> tokenProvider.regenerateAccessToken(tokenResponse.getRefreshToken()));
    //then
    assertEquals(customException.getErrorCode(), ErrorCode.NOT_FOUND_REFRESH_TOKEN);
  }

  @Test
  @DisplayName("AccessToken 재발행 시 기존 저장된 refreshToken과 불일치할 경우 예외 처리")
  void regenerateAccessToken_UnmatchedSavedRefreshToken() {
    //given
    String email = "abcd@abcd.com";
    List<String> roles = Collections.singletonList(Authority.ROLE_USER.toString());
    TokenResponse tokenResponse = tokenProvider.generateTokenResponse(email, roles);
    RefreshToken refreshToken = RefreshToken.builder()
        .id(email)
        .token("abcd")
        .build();

    given(refreshTokenRedisRepository.findById(anyString())).willReturn(Optional.of(refreshToken));
    //when
    CustomException customException = assertThrows(CustomException.class,
        () -> tokenProvider.regenerateAccessToken(tokenResponse.getRefreshToken()));
    //then
    assertEquals(customException.getErrorCode(), ErrorCode.UNMATCHED_SAVED_REFRESH_TOKEN);
  }

  @Test
  @DisplayName("토큰 유효성 검사 시 만료시간 경과된 토큰일 경우 예외 처리")
  void validateToken_InvalidToken() {
    //given
    Date now = new Date();
    Date beforeTwoHour = new Date(now.getTime() - (1000L * 60 * 60 * 2));
    Date beforeOneHour = new Date(now.getTime() - (1000L * 60 * 60));
    Claims claims = Jwts.claims().setSubject("abc@abcd.com");
    String token = Jwts.builder()
        .setClaims(claims)
        .setIssuedAt(beforeTwoHour)
        .setExpiration(beforeOneHour)
        .signWith(SignatureAlgorithm.HS256, tokenProvider.getSecretKey())
        .compact();
    //when
    CustomException customException = assertThrows(CustomException.class,
        () -> tokenProvider.validateToken(token));
    //then
    assertEquals(customException.getErrorCode(), ErrorCode.INVALID_TOKEN);
  }

  @Test
  @DisplayName("인증 획득 성공 테스트")
  void successGetAuthentication() {
    //given
    String email = "abcd@abcd.com";
    List<String> roles = Collections.singletonList(Authority.ROLE_USER.toString());
    TokenResponse tokenResponse = tokenProvider.generateTokenResponse(email, roles);

    Member member = Member.builder()
        .email("abcd@abcd.com")
        .roles(Collections.singletonList(Authority.ROLE_USER.toString()))
        .build();
    UserDetails userDetails = new CustomUserDetails(member);
    given(memberService.loadUserByUsername(anyString())).willReturn(userDetails);
    //when
    Authentication authentication = tokenProvider.getAuthentication(tokenResponse.getAccessToken());
    //then
    assertEquals(authentication.getPrincipal(), userDetails);
    assertEquals(authentication.getAuthorities(), userDetails.getAuthorities());
  }

  @Test
  @DisplayName("request로부터 순수 토큰 추출 성공 테스트(Bearer 제거)")
  void successIsAccessTokenDenied() {
    //given
    String email = "abcd@abcd.com";
    List<String> roles = Collections.singletonList(Authority.ROLE_USER.toString());
    TokenResponse tokenResponse = tokenProvider.generateTokenResponse(email, roles);
    String token = "Bearer " + tokenResponse.getAccessToken();
    //when
    String resolvedToken = tokenProvider.resolveTokenFromRequest(token);
    //then
    assertEquals(resolvedToken, tokenResponse.getAccessToken());
  }
}