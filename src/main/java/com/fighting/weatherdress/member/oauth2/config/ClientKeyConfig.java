package com.fighting.weatherdress.member.oauth2.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "spring.security.oauth2.client.registration.naver")
@Component
@Getter
@Setter
public class ClientKeyConfig {
  private String clientId;
  private String clientSecret;
}
