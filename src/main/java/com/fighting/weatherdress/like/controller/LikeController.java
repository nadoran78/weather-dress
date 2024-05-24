package com.fighting.weatherdress.like.controller;

import com.fighting.weatherdress.like.dto.LikeRegisterRequest;
import com.fighting.weatherdress.like.service.LikeService;
import com.fighting.weatherdress.security.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/like")
public class LikeController {

  private final LikeService likeService;

  @PostMapping
  public void registerLike(@RequestBody LikeRegisterRequest request,
      @AuthenticationPrincipal CustomUserDetails userDetails) {
    likeService.registerLike(request, userDetails.getId());
  }

}
