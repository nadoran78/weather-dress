package com.fighting.weatherdress.security.oauth2.controller;

import com.fighting.weatherdress.security.dto.TokenResponse;
import com.fighting.weatherdress.security.oauth2.dto.NaverProfile;
import com.fighting.weatherdress.security.oauth2.dto.OAuthToken;
import com.fighting.weatherdress.security.oauth2.service.NaverAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class NaverAuthController {

  private final NaverAuthService naverAuthService;

  @RequestMapping("/login/oauth2/code/naver")
  public ResponseEntity<TokenResponse> naverLogIn(@RequestParam String code,
      @RequestParam String state) {
    // 네이버서에서 토큰 받기
    OAuthToken oAuthToken = naverAuthService.getOAuthToken(code, state);

    // 사용자 정보 조회
    NaverProfile naverProfile = naverAuthService.getNaverProfile(oAuthToken.getAccess_token());

    // 멤버 등록 및 업데이트 후 토큰 발급
    TokenResponse tokenResponse = naverAuthService.saveOrUpdateMemberAndGetToken(naverProfile);

    return ResponseEntity.ok(tokenResponse);
  }
}
