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
public class WeeklyForecastItem {

  @JsonProperty("regId")
  private String regId;
  @JsonProperty("rnSt3Am")
  private int rainyPercentageAmPlus2Days;
  @JsonProperty("rnSt3Pm")
  private int rainyPercentagePmPlus2Days;
  @JsonProperty("rnSt4Am")
  private int rainyPercentageAmPlus3Days;
  @JsonProperty("rnSt4Pm")
  private int rainyPercentagePmPlus3Days;
  @JsonProperty("rnSt5Am")
  private int rainyPercentageAmPlus4Days;
  @JsonProperty("rnSt5Pm")
  private int rainyPercentagePmPlus4Days;
  @JsonProperty("rnSt6Am")
  private int rainyPercentageAmPlus5Days;
  @JsonProperty("rnSt6Pm")
  private int rainyPercentagePmPlus5Days;
  @JsonProperty("rnSt7Am")
  private int rainyPercentageAmPlus6Days;
  @JsonProperty("rnSt7Pm")
  private int rainyPercentagePmPlus6Days;
  @JsonProperty("rnSt8")
  private int rainyPercentagePlus7Days;
  @JsonProperty("rnSt9")
  private int rainyPercentagePlus8Days;
  @JsonProperty("rnSt10")
  private int rainyPercentagePlus9Days;

  @JsonProperty("wf3Am")
  private String weatherForecastAmPlus2Days;
  @JsonProperty("wf3Pm")
  private String weatherForecastPmPlus2Days;
  @JsonProperty("wf4Am")
  private String weatherForecastAmPlus3Days;
  @JsonProperty("wf4Pm")
  private String weatherForecastPmPlus3Days;
  @JsonProperty("wf5Am")
  private String weatherForecastAmPlus4Days;
  @JsonProperty("wf5Pm")
  private String weatherForecastPmPlus4Days;
  @JsonProperty("wf6Am")
  private String weatherForecastAmPlus5Days;
  @JsonProperty("wf6Pm")
  private String weatherForecastPmPlus5Days;
  @JsonProperty("wf7Am")
  private String weatherForecastAmPlus6Days;
  @JsonProperty("wf7Pm")
  private String weatherForecastPmPlus6Days;
  @JsonProperty("wf8")
  private String weatherForecastPlus7Days;
  @JsonProperty("wf9")
  private String weatherForecastPlus8Days;
  @JsonProperty("wf10")
  private String weatherForecastPlus9Days;

}
