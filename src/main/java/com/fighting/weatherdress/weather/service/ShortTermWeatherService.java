package com.fighting.weatherdress.weather.service;

import com.fighting.weatherdress.global.entity.Location;
import com.fighting.weatherdress.global.exception.CustomException;
import com.fighting.weatherdress.global.repository.LocationRepository;
import com.fighting.weatherdress.global.type.ErrorCode;
import com.fighting.weatherdress.weather.config.ShortTermWeatherProperties;
import com.fighting.weatherdress.weather.dto.DailyShortWeather;
import com.fighting.weatherdress.weather.dto.LocationDto;
import com.fighting.weatherdress.weather.dto.ShortTermWeatherResponse;
import com.fighting.weatherdress.weather.type.DressByTemperature;
import com.fighting.weatherdress.weather.type.PrecipitationTypeCode;
import com.fighting.weatherdress.weather.type.SkyCode;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class ShortTermWeatherService {

  private final CoordinateLocationConvertor coordinateLocationConvertor;
  private final ShortTermWeatherProperties shortTermWeatherProperties;
  private final LocationRepository locationRepository;
  private final RestTemplate restTemplate;

  private DailyShortWeather today;
  private DailyShortWeather tomorrow;
  private DailyShortWeather dayAfterTomorrow;

  public LocationDto convertCoordinateToLocation(double lat, double lng) {
    return coordinateLocationConvertor.convertCoordinateToLocation(lat,
        lng);
  }

  // 위경도를 통해 단기 날씨 조회
  @Cacheable(value = "shortTermWeather")
  @Transactional
  public ShortTermWeatherResponse getWeatherFromApi(String sido, String sigungu)
      throws URISyntaxException {
    LocalDateTime now = LocalDateTime.now();

    // today, tomorrow, dayAfterTomorrow 객체 초기화
    makeDailyShortWeathers(now);

    Location location = locationRepository.findBySidoAndSigungu(sido, sigungu)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_LOCATION));

    int pageNo = 1;
    boolean isDataNull = false;
    while (!isDataNull) {
      // 요청 uri 생성
      String requestUri = makeUri(pageNo, location.getXCoordinate(), location.getYCoordinate(),
          now);
      URI uri = new URI(requestUri);

      ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);
      String responseBody = response.getBody();

      // response 파싱 및 DailyShortWeather 객체에 데이터 입력
      isDataNull = parseResponseAndInputData(responseBody);
      pageNo++;
    }

    // DailyShortWeather 객체의 하루 중 최저 온도, 최고 온도, 평균 온도, 대표 하늘상태(비, 맑음 등) 설정
    setDailyShortWeatherField(today);
    setDailyShortWeatherField(tomorrow);
    setDailyShortWeatherField(dayAfterTomorrow);

    // 오늘, 내일, 모레를 ShortTermWeatherResponse 객체에 모아서 하나로 응답
    return ShortTermWeatherResponse.builder()
        .sido(location.getSido())
        .sigungu(location.getSigungu())
        .today(today)
        .tomorrow(tomorrow)
        .dayAfterTomorrow(dayAfterTomorrow)
        .build();
  }

  private void makeDailyShortWeathers(LocalDateTime now) {
    today = new DailyShortWeather(
        now.toLocalDate().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
    tomorrow = new DailyShortWeather(
        now.plusDays(1).toLocalDate().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
    dayAfterTomorrow = new DailyShortWeather(
        now.plusDays(2).toLocalDate().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
  }

  private String makeUri(int pageNo, int x, int y, LocalDateTime now) {
    String baseTime = getBaseTime(now);
    String baseDate = getBaseDate(now, baseTime);

    String requestUri = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst?";
    requestUri += "serviceKey=" + shortTermWeatherProperties.getServiceKey();
    requestUri += "&numOfRows=" + shortTermWeatherProperties.getNumOfRows();
    requestUri += "&pageNo=" + pageNo;
    requestUri += "&dataType=" + shortTermWeatherProperties.getDataType();
    requestUri += "&base_date=" + baseDate;
    requestUri += "&base_time=" + baseTime;
    requestUri += "&nx=" + x;
    requestUri += "&ny=" + y;

    return requestUri;
  }

  // baseTime 은 2시 부터 3시간 간격으로 리턴(ex. 0200, 0500, 0800)
  private String getBaseTime(LocalDateTime now) {
    int hour = now.getHour();
    int baseTime;
    if (hour % 3 < 2) {
      baseTime = hour - (hour % 3) - 1;
    } else {
      // 현재 시간이 5시 20분인 경우 baseTime은 02시 적용(baseTime 5시면 api 제공시간은 5시 20분 등 유동적이기 때문)
      baseTime = hour - 3;
    }

    if (baseTime < 2) {
      return "2300";
    } else if (baseTime < 10) {
      return "0" + baseTime + "00";
    } else {
      return baseTime + "00";
    }
  }

  private String getBaseDate(LocalDateTime now, String baseTime) {
    LocalDate today;
    if (baseTime.equals("2300")) {
      today = now.toLocalDate().minusDays(1);
    } else {
      today = now.toLocalDate();
    }
    return today.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
  }

  private boolean parseResponseAndInputData(String responseBody) {

    JsonElement element = JsonParser.parseString(responseBody);
    JsonObject object = element.getAsJsonObject();
    JsonObject response = object.getAsJsonObject("response");
    JsonObject header = response.getAsJsonObject("header");
    JsonElement resultCode = header.get("resultCode");
    if (resultCode.getAsString().equals("03")) {
      return true;
    }
    JsonObject body = response.getAsJsonObject("body");
    JsonObject items = body.getAsJsonObject("items");
    JsonArray item = items.getAsJsonArray("item");
    for (JsonElement ele : item) {
      JsonObject obj = ele.getAsJsonObject();
      JsonElement jsonCategory = obj.get("category");
      String category = jsonCategory.getAsString();
      JsonElement forecastDate = obj.get("fcstDate");
      JsonElement forecastValue = obj.get("fcstValue");
      JsonElement forecastTime = obj.get("fcstTime");
      switch (category) {
        case "TMP": {
          inputTemperatureData(forecastDate, forecastValue, forecastTime, today);
          inputTemperatureData(forecastDate, forecastValue, forecastTime, tomorrow);
          inputTemperatureData(forecastDate, forecastValue, forecastTime, dayAfterTomorrow);
          break;
        }
        case "SKY": {
          inputSkyStatusData(forecastDate, forecastValue, forecastTime, today);
          inputSkyStatusData(forecastDate, forecastValue, forecastTime, tomorrow);
          inputSkyStatusData(forecastDate, forecastValue, forecastTime, dayAfterTomorrow);
          break;
        }
        case "PTY": {
          inputPrecipitationTypeData(forecastDate, forecastValue, forecastTime, today);
          inputPrecipitationTypeData(forecastDate, forecastValue, forecastTime, tomorrow);
          inputPrecipitationTypeData(forecastDate, forecastValue, forecastTime, dayAfterTomorrow);
          break;
        }
      }
    }
    return false;
  }

  private void inputTemperatureData(JsonElement forecastDate, JsonElement forecastValue,
      JsonElement forecastTime, DailyShortWeather dailyShortWeather) {
    if (forecastDate.getAsString().equals(dailyShortWeather.getDate())) {
      dailyShortWeather.getHourlyTemperature()
          .put(forecastTime.getAsString(), Integer.parseInt(forecastValue.getAsString()));
    }
  }

  private void inputSkyStatusData(JsonElement forecastDate, JsonElement forecastValue,
      JsonElement forecastTime, DailyShortWeather dailyShortWeather) {
    if (forecastDate.getAsString().equals(dailyShortWeather.getDate())) {
      dailyShortWeather.getHourlySkyStatus()
          .put(forecastTime.getAsString(),
              SkyCode.findByCode(forecastValue.getAsString()).getSkyStatus());
    }
  }

  private void inputPrecipitationTypeData(JsonElement forecastDate, JsonElement forecastValue,
      JsonElement forecastTime, DailyShortWeather dailyShortWeather) {
    if (forecastDate.getAsString().equals(dailyShortWeather.getDate())) {
      dailyShortWeather.getHourlyPrecipitationType()
          .put(forecastTime.getAsString(),
              PrecipitationTypeCode.findByCode(forecastValue.getAsString()).getPrecipitationType());
    }
  }

  private void setDailyShortWeatherField(DailyShortWeather dailyShortWeather) {
    // 하루 중 최고온도, 최저온도, 평균온도 계산 및 입력
    Map<String, Integer> hourlyTemperatures = dailyShortWeather.getHourlyTemperature();

    int maxTemperature = Collections.max(hourlyTemperatures.values());
    int minTemperature = Collections.min(hourlyTemperatures.values());
    int averageTemperature = hourlyTemperatures.values().stream()
        .mapToInt(Integer::intValue)
        .sum() / hourlyTemperatures.size();

    dailyShortWeather.setMaxTemperature(maxTemperature);
    dailyShortWeather.setMinTemperature(minTemperature);
    dailyShortWeather.setAverageTemperature(averageTemperature);

    // 하루 중 강수형태(precipitation type)가 존재하면 하루의 대표날씨는 강수형태로 반환하고,
    // 강수형태가 존재하지 않으면 하루의 대표날씨는 하루의 시간별 하늘상태 중 가장 많은 하늘상태를 반환
    // ex. 강수형태에 비가 존재하면 하루의 대표날씨는 비로 반환
    // ex. 강수형태가 없고 시간별 하루상태 중 맑음이 가장 많았으면 하루의 대표날씨는 맑음 반환
    Map<String, String> hourlySkyStatus = dailyShortWeather.getHourlySkyStatus();
    Map<String, String> hourlyPrecipitationType = dailyShortWeather.getHourlyPrecipitationType();

    String averageSkyStatus;
    if (Collections.frequency(hourlyPrecipitationType.values(), "없음")
        == hourlyPrecipitationType.size()) {
      averageSkyStatus = getMaxFrequencyValue(hourlySkyStatus);
    } else {
      averageSkyStatus = getMaxFrequencyValue(hourlyPrecipitationType);
    }
    dailyShortWeather.setAverageSkyStatus(averageSkyStatus);

    // 평균기온에 따른 드레스 입력
    dailyShortWeather.setDress(DressByTemperature.findDressByTemperature(averageTemperature));
  }

  private String getMaxFrequencyValue(Map<String, String> map) {
    Map<String, Integer> countSky = new HashMap<>();

    String maxFrequencyValue = "";
    int maxCount = 0;
    for (String item : map.values()) {
      if (item.equals("없음")) {
        continue;
      }
      countSky.put(item, countSky.getOrDefault(item, 0) + 1);
      if (countSky.get(item) > maxCount) {
        maxCount = countSky.get(item);
        maxFrequencyValue = item;
      }
    }
    return maxFrequencyValue;
  }
}
