package com.fighting.weatherdress.member.oauth2.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class OAuthException extends RuntimeException{
  private int status;
  private String errorDescription;
}
