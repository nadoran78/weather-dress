package com.fighting.weatherdress.s3.repository;

import com.fighting.weatherdress.s3.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {

}
