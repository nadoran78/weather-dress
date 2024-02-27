package com.fighting.weatherdress.weather.dto.api.subclass;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Address {
  @JsonProperty("address_name")
  private String addressName;
  @JsonProperty("region_1depth_name")
  private String region1DepthName;
  @JsonProperty("region_2depth_name")
  private String region2DepthName;
  @JsonProperty("region_3depth_name")
  private String region3DepthName;
  @JsonProperty("region_3depth_h_name")
  private String region3DepthHName;
  @JsonProperty("h_code")
  private String hCode;
  @JsonProperty("b_code")
  private String bCode;
  @JsonProperty("mountain_yn")
  private String isMountain;
  @JsonProperty("main_address_no")
  private String mainAddressNo;
  @JsonProperty("sub_address_no")
  private String subAddressNo;

  private String x;
  private String y;

  }
