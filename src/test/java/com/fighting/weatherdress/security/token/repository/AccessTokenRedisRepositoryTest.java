package com.fighting.weatherdress.security.token.repository;

import com.fighting.weatherdress.security.token.entity.AccessToken;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class AccessTokenRedisRepositoryTest {

  @Autowired
  private AccessTokenRedisRepository accessTokenRedisRepository;

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
    accessTokenRedisRepository.deleteAll();
  }

  @Test
  @Transactional
  @DisplayName("생명주기 확인 테스트")
  void expireAccessToken() throws InterruptedException {
    String token = "aaaaaaaa";
    long expiration = 3L;
    AccessToken accessToken = AccessToken.builder().id("1111@abcd.com")
        .token(token)
        .expiration(expiration)
        .build();
    accessTokenRedisRepository.save(accessToken);
    Assertions.assertTrue(accessTokenRedisRepository.existsByToken(token));
    Thread.sleep(5 * 1000);
    Assertions.assertFalse(accessTokenRedisRepository.existsByToken(token));
  }
}