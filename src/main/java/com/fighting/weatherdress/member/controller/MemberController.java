package com.fighting.weatherdress.member.controller;

import com.fighting.weatherdress.global.type.CommonResponse;
import com.fighting.weatherdress.member.dto.SignUpDto;
import com.fighting.weatherdress.member.service.MemberService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

  private final MemberService memberService;

  @PostMapping("/sign-up")
  public ResponseEntity<CommonResponse> signUp(@Valid @RequestBody SignUpDto request)
      throws MessagingException {
    return ResponseEntity.ok().body(memberService.signUp(request));
  }

  @PostMapping("/verify")
  public ResponseEntity<CommonResponse> verifyEmail(@RequestBody String email,
      @RequestBody String code) {
    return ResponseEntity.ok().body(memberService.verifyEmail(email, code));
  }
}
