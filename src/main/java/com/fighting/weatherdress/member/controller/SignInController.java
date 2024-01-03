package com.fighting.weatherdress.member.controller;

import com.fighting.weatherdress.member.dto.SignInDto;
import com.fighting.weatherdress.member.service.SignInService;
import com.fighting.weatherdress.security.dto.TokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SignInController {

  private final SignInService signInService;

  @PostMapping("/sign-in")
  public ResponseEntity<TokenResponse> signIn(@RequestBody SignInDto request) {
    TokenResponse tokenResponse = signInService.signIn(request);
    return ResponseEntity.ok(tokenResponse);
  }
}
