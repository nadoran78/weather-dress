package com.fighting.weatherdress.mail.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "spring.mail")
@Component
@Getter
@Setter
public class EmailProperties {

  private String host;
  private int port;
  private String username;
  private String password;
  private boolean auth = true;
  private boolean ssl = true;
  private String sslTrust = "smtp.gmail.com";
}
