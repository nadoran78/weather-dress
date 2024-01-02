package com.fighting.weatherdress.security.oauth2.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.security.oauth2.client.registration.naver")
@Getter
@Setter
public class ClientKeyConfig {
  private String clientId;
  private String clientSecret;
}
