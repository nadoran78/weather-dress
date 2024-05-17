package com.fighting.weatherdress.security.oauth2.controller;

import com.fighting.weatherdress.member.oauth2.config.ClientKeyConfig;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Disabled
@SpringBootTest
class NaverAuthControllerTest {

  @Autowired
  private ClientKeyConfig clientKeyConfig;

  @Test
  @DisplayName("소셜 로그인 접속 url 생성(브라우저를 통해 직접 url 접속하여 테스트 성공)")
  void generateAuthUri() {
    String apiUri = "https://nid.naver.com/oauth2.0/authorize?response_type=code";
    String redirectUri = URLEncoder.encode("http://localhost:8080/login/oauth2/code/naver",
        StandardCharsets.UTF_8);
    SecureRandom random = new SecureRandom();
    String state = new BigInteger(130, random).toString();
    apiUri += "&client_id=" + clientKeyConfig.getClientId();
    apiUri += "&redirect_uri=" + redirectUri;
    apiUri += "&state=" + state;
    System.out.println(apiUri);
  }

}