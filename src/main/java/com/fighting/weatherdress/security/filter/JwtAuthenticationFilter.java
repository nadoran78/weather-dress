package com.fighting.weatherdress.security.filter;

import com.fighting.weatherdress.security.token.TokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private static final String TOKEN_HEADER = "Authorization";

  private final TokenProvider tokenProvider;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    String token = tokenProvider.resolveTokenFromRequest(request.getHeader(TOKEN_HEADER));

    if (StringUtils.hasText(token) && tokenProvider.validateToken(token)
        && !tokenProvider.isAccessTokenDenied(token)) {
      Authentication auth = tokenProvider.getAuthentication(token);
      SecurityContextHolder.getContext().setAuthentication(auth);
    } else {
      log.info("토큰 유효성 검증 실패");
    }

    filterChain.doFilter(request, response);
  }
}
