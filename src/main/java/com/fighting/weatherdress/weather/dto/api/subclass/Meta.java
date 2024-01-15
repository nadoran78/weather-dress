package com.fighting.weatherdress.weather.dto.api.subclass;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Meta {
  @JsonProperty("total_count")
  private int totalCount;
  @JsonProperty("pageable_count")
  private int pageableCount;
  @JsonProperty("is_end")
  private boolean isEnd;
}
