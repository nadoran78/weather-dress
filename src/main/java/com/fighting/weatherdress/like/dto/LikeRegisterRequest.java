package com.fighting.weatherdress.like.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class LikeRegisterRequest {

  private Long postId;

  private Long replyId;

}
