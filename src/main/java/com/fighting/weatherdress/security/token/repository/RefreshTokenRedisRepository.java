package com.fighting.weatherdress.security.token.repository;

import com.fighting.weatherdress.security.token.entity.RefreshToken;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRedisRepository extends CrudRepository<RefreshToken, String> {

}
