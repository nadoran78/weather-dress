package com.fighting.weatherdress.weather.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ConvertMode {
  TO_GRID(0),
  TO_GPS(1);

  private final int code;

}
