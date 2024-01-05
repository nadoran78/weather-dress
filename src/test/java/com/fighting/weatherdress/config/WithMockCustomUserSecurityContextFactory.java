package com.fighting.weatherdress.config;

import com.fighting.weatherdress.global.type.Authority;
import com.fighting.weatherdress.member.domain.Member;
import com.fighting.weatherdress.security.dto.CustomUserDetails;
import java.util.Collections;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithMockCustomUserSecurityContextFactory implements
    WithSecurityContextFactory<WithMockCustomUser> {

  @Override
  public SecurityContext createSecurityContext(WithMockCustomUser customUser) {
    SecurityContext context = SecurityContextHolder.createEmptyContext();

    Member member = Member.builder()
        .email(customUser.email())
        .password(customUser.password())
        .nickName("nickname")
        .verified(true)
        .roles(Collections.singletonList(Authority.ROLE_USER.toString()))
        .build();

    CustomUserDetails principal = new CustomUserDetails(member);
    Authentication authentication = new UsernamePasswordAuthenticationToken(principal, "",
        principal.getAuthorities());
    context.setAuthentication(authentication);
    return context;
  }
}
