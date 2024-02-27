package com.fighting.weatherdress.weather.dto.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fighting.weatherdress.weather.dto.api.subclass.Documents;
import com.fighting.weatherdress.weather.dto.api.subclass.Meta;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CoordinateLocationConvertResponse {
  @JsonProperty("meta")
  private Meta meta;
  @JsonProperty("documents")
  private List<Documents> documents;
}
