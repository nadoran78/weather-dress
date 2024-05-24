package com.fighting.weatherdress.like.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class LikeRegisterRequest {

  private Long postId;

  private Long replyId;

}
