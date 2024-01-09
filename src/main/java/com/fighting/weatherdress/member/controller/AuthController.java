package com.fighting.weatherdress.member.controller;

import com.fighting.weatherdress.global.type.CommonResponse;
import com.fighting.weatherdress.member.dto.SignInDto;
import com.fighting.weatherdress.member.service.AuthService;
import com.fighting.weatherdress.security.dto.CustomUserDetails;
import com.fighting.weatherdress.security.dto.TokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;

  @PostMapping("/sign-in")
  public ResponseEntity<TokenResponse> signIn(@RequestBody SignInDto request) {
    TokenResponse tokenResponse = authService.signIn(request);
    return ResponseEntity.ok(tokenResponse);
  }

  @PostMapping("/sign-out")
  public ResponseEntity<CommonResponse> signOut(
      @AuthenticationPrincipal CustomUserDetails userDetails,
      @RequestHeader("Authorization") String accessToken) {
    authService.signOut(accessToken, userDetails.getEmail());
    return ResponseEntity.ok(CommonResponse.SUCCESS);
  }

  @PostMapping("/sign-in/reissue")
  public ResponseEntity<TokenResponse> reissueToken(@RequestHeader("RefreshToken") String refreshToken) {
    TokenResponse tokenResponse = authService.reissueToken(refreshToken);
    return ResponseEntity.ok(tokenResponse);
  }
}
