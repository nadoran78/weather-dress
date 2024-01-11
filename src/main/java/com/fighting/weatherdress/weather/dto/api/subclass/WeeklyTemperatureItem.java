package com.fighting.weatherdress.weather.dto.api.subclass;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class WeeklyTemperatureItem {
  @JsonProperty("regId")
  private String regId;
  @JsonProperty("taMin3")
  private int minTemperaturePlus2Days;
  @JsonProperty("taMin3Low")
  private int minTemperaturePlus2DaysLow;
  @JsonProperty("taMin3High")
  private int minTemperaturePlus2DaysHigh;
  @JsonProperty("taMax3")
  private int maxTemperaturePlus2Days;
  @JsonProperty("taMax3Low")
  private int maxTemperaturePlus2DaysLow;
  @JsonProperty("taMax3High")
  private int maxTemperaturePlus2DaysHigh;

  @JsonProperty("taMin4")
  private int minTemperaturePlus3Days;
  @JsonProperty("taMin4Low")
  private int minTemperaturePlus3DaysLow;
  @JsonProperty("taMin4High")
  private int minTemperaturePlus3DaysHigh;
  @JsonProperty("taMax4")
  private int maxTemperaturePlus3Days;
  @JsonProperty("taMax4Low")
  private int maxTemperaturePlus3DaysLow;
  @JsonProperty("taMax4High")
  private int maxTemperaturePlus3DaysHigh;

  @JsonProperty("taMin5")
  private int minTemperaturePlus4Days;
  @JsonProperty("taMin5Low")
  private int minTemperaturePlus4DaysLow;
  @JsonProperty("taMin5High")
  private int minTemperaturePlus4DaysHigh;
  @JsonProperty("taMax5")
  private int maxTemperaturePlus4Days;
  @JsonProperty("taMax5Low")
  private int maxTemperaturePlus4DaysLow;
  @JsonProperty("taMax5High")
  private int maxTemperaturePlus4DaysHigh;

  @JsonProperty("taMin6")
  private int minTemperaturePlus5Days;
  @JsonProperty("taMin6Low")
  private int minTemperaturePlus5DaysLow;
  @JsonProperty("taMin6High")
  private int minTemperaturePlus5DaysHigh;
  @JsonProperty("taMax6")
  private int maxTemperaturePlus5Days;
  @JsonProperty("taMax6Low")
  private int maxTemperaturePlus5DaysLow;
  @JsonProperty("taMax6High")
  private int maxTemperaturePlus5DaysHigh;

  @JsonProperty("taMin7")
  private int minTemperaturePlus6Days;
  @JsonProperty("taMin7Low")
  private int minTemperaturePlus6DaysLow;
  @JsonProperty("taMin7High")
  private int minTemperaturePlus6DaysHigh;
  @JsonProperty("taMax7")
  private int maxTemperaturePlus6Days;
  @JsonProperty("taMax7Low")
  private int maxTemperaturePlus6DaysLow;
  @JsonProperty("taMax7High")
  private int maxTemperaturePlus6DaysHigh;

  @JsonProperty("taMin8")
  private int minTemperaturePlus7Days;
  @JsonProperty("taMin8Low")
  private int minTemperaturePlus7DaysLow;
  @JsonProperty("taMin8High")
  private int minTemperaturePlus7DaysHigh;
  @JsonProperty("taMax8")
  private int maxTemperaturePlus7Days;
  @JsonProperty("taMax8Low")
  private int maxTemperaturePlus7DaysLow;
  @JsonProperty("taMax8High")
  private int maxTemperaturePlus7DaysHigh;

  @JsonProperty("taMin9")
  private int minTemperaturePlus8Days;
  @JsonProperty("taMin9Low")
  private int minTemperaturePlus8DaysLow;
  @JsonProperty("taMin9High")
  private int minTemperaturePlus8DaysHigh;
  @JsonProperty("taMax9")
  private int maxTemperaturePlus8Days;
  @JsonProperty("taMax9Low")
  private int maxTemperaturePlus8DaysLow;
  @JsonProperty("taMax9High")
  private int maxTemperaturePlus8DaysHigh;

  @JsonProperty("taMin10")
  private int minTemperaturePlus9Days;
  @JsonProperty("taMin10Low")
  private int minTemperaturePlus9DaysLow;
  @JsonProperty("taMin10High")
  private int minTemperaturePlus9DaysHigh;
  @JsonProperty("taMax10")
  private int maxTemperaturePlus9Days;
  @JsonProperty("taMax10Low")
  private int maxTemperaturePlus9DaysLow;
  @JsonProperty("taMax10High")
  private int maxTemperaturePlus9DaysHigh;

}
