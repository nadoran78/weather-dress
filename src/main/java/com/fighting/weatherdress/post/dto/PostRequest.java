package com.fighting.weatherdress.post.dto;

import com.fighting.weatherdress.weather.dto.LocationDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@Builder
@EqualsAndHashCode
public class PostRequest {

  @NotBlank
  @Size(max = 300)
  private final String content;

  @NotNull
  private final LocationDto location;

}
