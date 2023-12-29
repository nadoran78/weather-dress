package com.fighting.weatherdress.security.token.repository;

import com.fighting.weatherdress.security.token.entity.AccessToken;
import org.springframework.data.repository.CrudRepository;

public interface AccessTokenRedisRepository extends CrudRepository<AccessToken, String> {
  boolean existsByToken(String token);
}
