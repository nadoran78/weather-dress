package com.fighting.weatherdress.member.controller;

import com.fighting.weatherdress.global.type.CommonResponse;
import com.fighting.weatherdress.member.dto.ChangePasswordDto;
import com.fighting.weatherdress.member.service.MemberInfoService;
import com.fighting.weatherdress.security.dto.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberInfoController {

  private final MemberInfoService memberInfoService;

  @PostMapping("/password-change")
  public ResponseEntity<CommonResponse> changePassword(
      @AuthenticationPrincipal CustomUserDetails customUserDetails,
      @Valid @RequestBody ChangePasswordDto request) {
    memberInfoService.changePassword(customUserDetails.getEmail(), request);
    return ResponseEntity.ok(CommonResponse.SUCCESS);
  }

}
