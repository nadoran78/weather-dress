package com.fighting.weatherdress.reply.controller;

import com.fighting.weatherdress.reply.dto.ReplyRequest;
import com.fighting.weatherdress.reply.service.ReplyService;
import com.fighting.weatherdress.security.dto.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reply")
public class ReplyController {

  private final ReplyService replyService;

  @PostMapping
  public void registerReply(@Valid @RequestBody ReplyRequest request,
      @AuthenticationPrincipal CustomUserDetails userDetails) {
    replyService.registerReply(request, userDetails.getId());
  }

}
