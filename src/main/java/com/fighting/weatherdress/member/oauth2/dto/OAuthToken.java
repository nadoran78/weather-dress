package com.fighting.weatherdress.member.oauth2.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class OAuthToken {
  private String access_token;
  private String refresh_token;
  private String token_type;
  private String expires_in;
  private String error;
  private String error_description;
}
