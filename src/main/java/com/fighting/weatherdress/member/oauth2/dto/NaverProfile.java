package com.fighting.weatherdress.member.oauth2.dto;

import com.fighting.weatherdress.global.type.Authority;
import com.fighting.weatherdress.member.domain.Member;
import java.util.Collections;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class NaverProfile {
  private String email;
  private String name;
  private String error;
  private String error_description;

  public Member toEntity() {
    return Member.builder()
        .email(email)
        .nickName(name)
        .verified(true)
        .roles(Collections.singletonList(Authority.ROLE_USER.toString()))
        .build();
  }
}
