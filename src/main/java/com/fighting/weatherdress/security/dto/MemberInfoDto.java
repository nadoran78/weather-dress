package com.fighting.weatherdress.security.dto;

import com.fighting.weatherdress.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberInfoDto {
  private Long id;
  private String email;
  private String nickName;

  public static MemberInfoDto fromEntity(Member member) {
    return MemberInfoDto.builder()
        .id(member.getId())
        .email(member.getEmail())
        .nickName(member.getNickName())
        .build();
  }
}
