package com.fighting.weatherdress.weather.dto.api.subclass;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Header {
  private String resultCode;
  private String resultMsg;
}
