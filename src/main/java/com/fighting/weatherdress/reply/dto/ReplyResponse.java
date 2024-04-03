package com.fighting.weatherdress.reply.dto;

import com.fighting.weatherdress.reply.entity.Reply;
import com.fighting.weatherdress.security.dto.MemberInfoDto;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ReplyResponse {

  private final long replyId;

  private final String text;

  private final MemberInfoDto member;

  private final Long postId;

  private final LocalDateTime createdAt;

  public static ReplyResponse fromEntity(Reply reply) {
    return ReplyResponse.builder()
        .replyId(reply.getId())
        .text(reply.getText())
        .member(MemberInfoDto.fromEntity(reply.getMember()))
        .postId(reply.getPost().getId())
        .createdAt(reply.getCreatedAt())
        .build();
  }

}
