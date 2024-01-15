package com.fighting.weatherdress.weather.dto.api.subclass;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Documents {
  @JsonProperty("address_name")
  private String addressName;
  @JsonProperty("y")
  private String y;
  @JsonProperty("x")
  private String x;
  @JsonProperty("address_type")
  private String addressType;
  @JsonProperty("address")
  private Address address;
  @JsonProperty("road_address")
  private RoadAddress roadAddress;
}
