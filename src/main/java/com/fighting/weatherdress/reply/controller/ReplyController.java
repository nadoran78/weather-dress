package com.fighting.weatherdress.reply.controller;

import com.fighting.weatherdress.reply.dto.ReplyListDto;
import com.fighting.weatherdress.reply.dto.ReplyRegisterRequest;
import com.fighting.weatherdress.reply.dto.ReplyResponse;
import com.fighting.weatherdress.reply.dto.ReplyUpdateRequest;
import com.fighting.weatherdress.reply.service.ReplyService;
import com.fighting.weatherdress.security.dto.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reply")
public class ReplyController {

  private final ReplyService replyService;

  @PostMapping
  public void registerReply(@Valid @RequestBody ReplyRegisterRequest request,
      @AuthenticationPrincipal CustomUserDetails userDetails) {
    replyService.registerReply(request, userDetails.getId());
  }

  @GetMapping("/{replyId}")
  public ReplyResponse getReply(@PathVariable long replyId) {
    return replyService.getReply(replyId);
  }

  @GetMapping("/list/{postId}")
  public Slice<ReplyListDto> getReplyList(@PathVariable long postId,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size,
      @RequestParam(defaultValue = "desc") String direction) {
    PageRequest pageRequest = PageRequest.of(page, size, Direction.fromString(direction),
        "createdAt");
    return replyService.getReplyList(postId, pageRequest);
  }

  @PatchMapping("/{replyId}")
  public ReplyResponse updateReply(@PathVariable long replyId,
      @RequestBody ReplyUpdateRequest request,
      @AuthenticationPrincipal CustomUserDetails userDetails) {
    return replyService.updateReply(replyId, request, userDetails.getId());
  }

}
