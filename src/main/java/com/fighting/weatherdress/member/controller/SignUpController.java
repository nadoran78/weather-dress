package com.fighting.weatherdress.member.controller;

import com.fighting.weatherdress.global.type.CommonResponse;
import com.fighting.weatherdress.member.dto.SignUpDto;
import com.fighting.weatherdress.member.dto.VerifyEmailDto;
import com.fighting.weatherdress.member.service.SignUpService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SignUpController {

  private final SignUpService signUpService;

  @PostMapping("/sign-up")
  public ResponseEntity<CommonResponse> signUp(@Valid @RequestBody SignUpDto request)
      throws MessagingException {
    signUpService.signUp(request);
    return ResponseEntity.ok().body(CommonResponse.SUCCESS);
  }

  @PostMapping("/email")
  @PreAuthorize(value = "hasRole('USER')")
  public ResponseEntity<CommonResponse> sendEmailWithVerifyCode(@RequestBody String email)
      throws MessagingException {
    signUpService.sendEmail(email);
    return ResponseEntity.ok().body(CommonResponse.SUCCESS);
  }

  @PostMapping("/verify")
  public ResponseEntity<CommonResponse> verifyEmail(@RequestBody VerifyEmailDto request) {
    signUpService.verifyEmail(request.getEmail(), request.getCode());
    return ResponseEntity.ok().body(CommonResponse.SUCCESS);
  }
}
