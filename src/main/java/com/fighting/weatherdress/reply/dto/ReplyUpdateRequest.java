package com.fighting.weatherdress.reply.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@Builder
@EqualsAndHashCode
public class ReplyUpdateRequest {

  @Size(max = 100)
  private final String text;

  @JsonCreator
  public ReplyUpdateRequest (String text) {
    this.text = text;
  }

}