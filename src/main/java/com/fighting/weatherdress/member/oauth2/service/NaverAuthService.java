package com.fighting.weatherdress.member.oauth2.service;

import com.fighting.weatherdress.member.domain.Member;
import com.fighting.weatherdress.member.repository.MemberRepository;
import com.fighting.weatherdress.security.dto.TokenResponse;
import com.fighting.weatherdress.member.oauth2.exception.OAuthException;
import com.fighting.weatherdress.member.oauth2.config.ClientKeyConfig;
import com.fighting.weatherdress.member.oauth2.dto.NaverProfile;
import com.fighting.weatherdress.member.oauth2.dto.OAuthToken;
import com.fighting.weatherdress.security.token.TokenProvider;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Service
public class NaverAuthService {

  private final ClientKeyConfig clientKeyConfig;
  private final MemberRepository memberRepository;
  private final TokenProvider tokenProvider;

  public OAuthToken getOAuthToken(String code, String state) {
    String requestUri = "https://nid.naver.com/oauth2.0/token";

    RestTemplate restTemplate = new RestTemplate();

    HttpEntity<MultiValueMap<String, String>> tokenRequest = makeHttpEntity(code, state);

    ResponseEntity<String> response = restTemplate.exchange(requestUri, HttpMethod.POST,
        tokenRequest, String.class);

    int responseStatusCode = response.getStatusCode().value();
    String responseBody = response.getBody();

    Gson gson = new Gson();
    OAuthToken oAuthToken = gson.fromJson(responseBody, OAuthToken.class);

    if (responseStatusCode != 200) {
      throw new OAuthException(responseStatusCode, oAuthToken.getError_description());
    }
    return oAuthToken;
  }

  private HttpEntity<MultiValueMap<String, String>> makeHttpEntity(String code, String state) {
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

  public NaverProfile getNaverProfile(String accessToken) {
    String tokenHeader = "Bearer " + accessToken;
    HttpHeaders headers = new HttpHeaders();
    headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
    headers.add("Authorization", tokenHeader);

    String requestUri = "https://openapi.naver.com/v1/nid/me";

    RestTemplate restTemplate = new RestTemplate();
    ResponseEntity<String> response = restTemplate.exchange(requestUri, HttpMethod.GET,
        new HttpEntity<>(headers), String.class);

    int responseStatusCode = response.getStatusCode().value();
    String responseBody = response.getBody();

    Gson gson = new Gson();
    NaverProfile naverProfile;
    if (responseStatusCode != 200) {
      naverProfile = gson.fromJson(responseBody, NaverProfile.class);
      throw new OAuthException(responseStatusCode, naverProfile.getError_description());
    }

    assert responseBody != null;
    JsonElement element = JsonParser.parseString(responseBody);
    JsonObject object = element.getAsJsonObject();
    JsonObject responseJsonObject = object.getAsJsonObject("response");
    naverProfile = gson.fromJson(responseJsonObject, NaverProfile.class);
    return naverProfile;
  }

  // 멤버 업데이트 및 저장 후 토큰 발급
  @Transactional
  public TokenResponse saveOrUpdateMemberAndGetToken(NaverProfile naverProfile) {
    Member member = memberRepository.findByEmail(naverProfile.getEmail())
        .orElse(naverProfile.toEntity());

    if (!Objects.equals(member.getNickName(), naverProfile.getName())) {
      member.setNickName(naverProfile.getName());
    }

    Member savedMember = memberRepository.save(member);

    return tokenProvider.generateTokenResponse(savedMember.getEmail(), savedMember.getRoles());
  }
}
