package com.fighting.weatherdress.reply.dto;

import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@Builder
@EqualsAndHashCode
public class ReplyRegisterRequest {

  private final long postId;

  @Size(max = 100)
  private final String text;
}
