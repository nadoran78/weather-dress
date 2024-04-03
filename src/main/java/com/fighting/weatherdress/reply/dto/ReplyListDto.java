package com.fighting.weatherdress.reply.dto;

import com.fighting.weatherdress.reply.entity.Reply;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ReplyListDto {

  private final long replyId;

  private final String text;

  private final String memberNickname;

  private final LocalDateTime createdAt;

  public static ReplyListDto fromEntity(Reply reply) {
    return ReplyListDto.builder()
        .replyId(reply.getId())
        .text(reply.getText())
        .memberNickname(reply.getMember().getNickName())
        .createdAt(reply.getCreatedAt())
        .build();
  }

}
