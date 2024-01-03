package com.fighting.weatherdress.security.token;

import com.fighting.weatherdress.global.exception.CustomException;
import com.fighting.weatherdress.global.type.ErrorCode;
import com.fighting.weatherdress.member.service.MemberService;
import com.fighting.weatherdress.security.dto.TokenResponse;
import com.fighting.weatherdress.security.token.entity.AccessToken;
import com.fighting.weatherdress.security.token.entity.RefreshToken;
import com.fighting.weatherdress.security.token.repository.AccessTokenRedisRepository;
import com.fighting.weatherdress.security.token.repository.RefreshTokenRedisRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import jakarta.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@Getter
@RequiredArgsConstructor
public class TokenProvider {

  private final MemberService memberService;
  private final AccessTokenRedisRepository accessTokenRedisRepository;
  private final RefreshTokenRedisRepository refreshTokenRedisRepository;

  @Value("${spring.jwt.secret}")
  private String secretKey;

  public static final String TOKEN_PREFIX = "Bearer ";
  private static final String KEY_ROLES = "roles";
  private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000L * 60 * 60; // 1시간
  private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000L * 60 * 60 * 24 * 30; //30일

  // secretKey를 Base64 형식으로 인코딩하여 사용(charset 오류 방지 목적)
  @PostConstruct
  protected void init() {
    secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes(StandardCharsets.UTF_8));
  }

  public TokenResponse generateTokenResponse(String email, List<String> roles) {
    Claims claims = Jwts.claims().setSubject(email);
    claims.put(KEY_ROLES, roles);

    String accessToken = generateToken(claims, ACCESS_TOKEN_EXPIRE_TIME);
    String refreshToken = generateToken(claims, REFRESH_TOKEN_EXPIRE_TIME);

    refreshTokenRedisRepository.save(new RefreshToken(email, refreshToken));

    return TokenResponse.builder()
        .email(email)
        .accessToken(accessToken)
        .refreshToken(refreshToken)
        .build();
  }

  private String generateToken(Claims claims, Long expiredTime) {
    Date now = new Date();

    return Jwts.builder()
        .setClaims(claims)
        .setIssuedAt(now)
        .setExpiration(new Date(now.getTime() + expiredTime))
        .signWith(SignatureAlgorithm.HS256, secretKey)
        .compact();
  }

  public TokenResponse regenerateAccessToken(String refreshToken) {
    if (!validateToken(refreshToken)) {
      throw new CustomException(ErrorCode.INVALID_TOKEN);
    }
    Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(refreshToken).getBody();

    String email = claims.getSubject();

    String findToken = refreshTokenRedisRepository.findById(email)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_REFRESH_TOKEN)).getToken();
    if (!refreshToken.equals(findToken)) {
      throw new CustomException(ErrorCode.UNMATCHED_SAVED_REFRESH_TOKEN);
    }

    String accessToken = generateToken(claims, ACCESS_TOKEN_EXPIRE_TIME);

    return TokenResponse.builder()
        .email(email)
        .accessToken(accessToken)
        .refreshToken(refreshToken)
        .build();
  }

  public boolean validateToken(String token) {
    if (!StringUtils.hasText(token)) {
      return false;
    }
    try {
      Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);

      return !claims.getBody().getExpiration().before(new Date());
    } catch (ExpiredJwtException | MalformedJwtException | SignatureException e) {
      throw new CustomException(ErrorCode.INVALID_TOKEN);
    }
  }

  public Authentication getAuthentication(String token) {
    String email = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token)
        .getBody().getSubject();
    UserDetails userDetails = memberService.loadUserByUsername(email);

    return new UsernamePasswordAuthenticationToken(
        userDetails, "", userDetails.getAuthorities());
  }

  public boolean isAccessTokenDenied(String accessToken) {
    return accessTokenRedisRepository.existsByToken(accessToken);
  }

  public String resolveTokenFromRequest(String token) {
    if (StringUtils.hasText(token) && token.startsWith(TOKEN_PREFIX)) {
      return token.substring(TOKEN_PREFIX.length());
    }
    return null;
  }

  public void deleteRefreshToken(String email) {
    RefreshToken refreshToken = refreshTokenRedisRepository.findById(email)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_REFRESH_TOKEN));
    refreshTokenRedisRepository.delete(refreshToken);
  }

  public void addBlackList(String accessToken, String email) {
    long expirationSeconds = getTokenExpireTime(accessToken);
    accessTokenRedisRepository.save(new AccessToken(email, accessToken, expirationSeconds));
  }

  private long getTokenExpireTime(String token) {
    Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
    return (claims.getBody().getExpiration().getTime() - new Date().getTime()) * 1000;
  }
}
