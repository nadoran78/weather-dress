package com.fighting.weatherdress.security.token.repository;

import com.fighting.weatherdress.security.token.entity.RefreshToken;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class RefreshTokenRedisRepositoryTest {

  @Autowired
  private RefreshTokenRedisRepository refreshTokenRedisRepository;

  @Test
  void successfindById() {
    //given
    String email1 = "1111@abcd.com";
    String email2 = "2222@abcd.com";
    RefreshToken refreshToken1 = RefreshToken.builder().id(email1)
        .token("aaaaaaaa")
        .build();
    RefreshToken refreshToken2 = RefreshToken.builder().id(email2)
        .token("bbbbbbbb")
        .build();
    //when
    RefreshToken saved1 = refreshTokenRedisRepository.save(refreshToken1);
    RefreshToken saved2 = refreshTokenRedisRepository.save(refreshToken2);

    RefreshToken refreshToken = refreshTokenRedisRepository.findById(email1).get();
    //then
    Assertions.assertEquals(refreshToken.getId(), email1);
    refreshTokenRedisRepository.delete(saved1);
    refreshTokenRedisRepository.delete(saved2);
  }
}