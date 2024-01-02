package com.fighting.weatherdress.security.oauth2.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fighting.weatherdress.security.oauth2.config.ClientKeyConfig;
import com.fighting.weatherdress.security.oauth2.dto.OAuthToken;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Component
public class NaverAuthApi {

  private final ClientKeyConfig clientKeyConfig;

  public OAuthToken getOAuthToken(String code, String state) throws JsonProcessingException {
    String requestUri = "https://nid.naver.com/oauth2.0/token";

    RestTemplate restTemplate = new RestTemplate();

    HttpEntity<MultiValueMap<String, String>> tokenRequest = makeHttpEntity(code, state);

    ResponseEntity<String> response = restTemplate.exchange(requestUri, HttpMethod.POST,
        tokenRequest, String.class);

    String responseBody = response.getBody();

    ObjectMapper objectMapper = new ObjectMapper();

    return objectMapper.readValue(responseBody, OAuthToken.class);
  }

  private HttpEntity<MultiValueMap<String, String>> makeHttpEntity(String code,
      String state) {
    HttpHeaders headers = new HttpHeaders();
    headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    params.add("grant_type", "authorization_code");
    params.add("client_id", clientKeyConfig.getClientId());
    params.add("client_secret", clientKeyConfig.getClientSecret());
    params.add("code", code);
    params.add("state", state);

    return new HttpEntity<>(params, headers);
  }

}
