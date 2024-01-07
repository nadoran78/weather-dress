package com.fighting.weatherdress.global.repository;


import com.fighting.weatherdress.global.entity.Location;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
  Optional<Location> findBySidoAndSigungu(String sido, String sigungu);
}
