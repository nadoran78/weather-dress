package com.fighting.weatherdress.reply.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ReplyUpdateRequest {

  @Size(max = 100)
  private final String text;

}
