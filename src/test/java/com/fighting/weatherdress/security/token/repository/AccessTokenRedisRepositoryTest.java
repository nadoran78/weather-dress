package com.fighting.weatherdress.security.token.repository;

import com.fighting.weatherdress.security.token.entity.AccessToken;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class AccessTokenRedisRepositoryTest {

  @Autowired
  private AccessTokenRedisRepository accessTokenRedisRepository;

  @AfterEach
  void rollBack() {
    accessTokenRedisRepository.deleteAll();
  }

  @Test
  void successExistsByToken() {
    //given
    String token1 = "aaaaaaaa";
    String token2 = "bbbbbbbb";
    AccessToken accessToken1 = AccessToken.builder().id("1111@abcd.com")
        .token(token1)
        .build();
    AccessToken accessToken2 = AccessToken.builder().id("2222@abcd.com")
        .token(token2)
        .build();
    //when
    accessTokenRedisRepository.save(accessToken1);
    accessTokenRedisRepository.save(accessToken2);
    //then
    Assertions.assertTrue(accessTokenRedisRepository.existsByToken(token1));
  }
}