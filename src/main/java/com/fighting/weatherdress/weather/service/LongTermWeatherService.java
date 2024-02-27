package com.fighting.weatherdress.weather.service;

import com.fighting.weatherdress.global.entity.Location;
import com.fighting.weatherdress.global.exception.CustomException;
import com.fighting.weatherdress.global.repository.LocationRepository;
import com.fighting.weatherdress.global.type.ErrorCode;
import com.fighting.weatherdress.weather.config.LongTermWeatherProperties;
import com.fighting.weatherdress.weather.dto.LongTermWeatherResponse;
import com.fighting.weatherdress.weather.dto.WeeklyWeatherDto;
import com.fighting.weatherdress.weather.dto.api.ResponseFromWeeklyForecastApi;
import com.fighting.weatherdress.weather.dto.api.ResponseFromWeeklyTemperatureApi;
import com.fighting.weatherdress.weather.dto.api.subclass.WeeklyForecastItem;
import com.fighting.weatherdress.weather.dto.api.subclass.WeeklyTemperatureItem;
import com.fighting.weatherdress.weather.entity.WeeklyWeather;
import com.fighting.weatherdress.weather.repository.WeeklyWeatherRepository;
import java.lang.reflect.Field;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class LongTermWeatherService {

  private final LongTermWeatherProperties longTermWeatherProperties;
  private final LocationRepository locationRepository;
  private final RestTemplate restTemplate;
  private final WeeklyWeatherRepository weeklyWeatherRepository;

  // 오전 날씨예보 발표시간
  private final LocalTime MORNING_STANDARD_TIME = LocalTime.of(6, 0);
  // 오후 날씨예보 발표시간
  private final LocalTime EVENING_STANDARD_TIME = LocalTime.of(18, 0);

  public LongTermWeatherResponse getLongTermWeather(String sido, String sigungu) {
    Location location = locationRepository.findBySidoAndSigungu(sido, sigungu)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_LOCATION));

    return LongTermWeatherResponse.builder()
        .sido(location.getSido())
        .sigungu(location.getSigungu())
        .weeklyWeathers(
            location.getWeeklyWeathers()
                .stream()
                .map(WeeklyWeatherDto::fromEntity)
                .sorted((Comparator.comparing(WeeklyWeatherDto::getDate)))
                .toList())
        .build();
  }

  // 중기 예보 db 저장 기능 - 향후 spring batch 적용 예정
  @Scheduled(cron = "${schedules.cron.update.weather}")
  @Transactional
  public void updateLongTermWeather()
      throws URISyntaxException, NoSuchFieldException, IllegalAccessException {
    LocalDateTime now = LocalDateTime.now();

    List<Location> allLocation = locationRepository.findAll();

    for (Location location : allLocation) {
      WeeklyForecastItem forecast = getWeeklyForecastFromApi(now,
          location.getLocationLandCode());
      WeeklyTemperatureItem temperature = getWeeklyTemperatureFromApi(now,
          location.getLocationCode());

      location.getWeeklyWeathers().clear();

      for (int i = 2; i <= 6; i++) {
        String minTemperature = "minTemperaturePlus" + i + "Days";
        String maxTemperature = "maxTemperaturePlus" + i + "Days";
        String morningForecast = "weatherForecastAmPlus" + i + "Days";
        String afternoonForecast = "weatherForecastPmPlus" + i + "Days";

        WeeklyWeather weeklyWeather = WeeklyWeather.builder()
            .date(now.toLocalDate().plusDays(i))
            .minTemperature(
                getField(temperature.getClass(), minTemperature).getInt(temperature))
            .maxTemperature(
                getField(temperature.getClass(), maxTemperature).getInt(temperature))
            .morningWeatherForecast(
                getField(forecast.getClass(), morningForecast).get(forecast).toString())
            .afternoonWeatherForecast(
                getField(forecast.getClass(), afternoonForecast).get(forecast).toString())
            .location(location)
            .build();

        weeklyWeatherRepository.save(weeklyWeather);
      }
    }
  }

  private Field getField(Class<?> inputClass, String fieldName)
      throws NoSuchFieldException {
    Field field = inputClass.getDeclaredField(fieldName);
    field.setAccessible(true);
    return field;
  }

  private WeeklyForecastItem getWeeklyForecastFromApi(LocalDateTime now,
      String locationLandCode) throws URISyntaxException {
    URI uri = makeWeeklyForecastUri(locationLandCode, now);

    ResponseEntity<ResponseFromWeeklyForecastApi> responseEntity = restTemplate.getForEntity(uri,
        ResponseFromWeeklyForecastApi.class);
    ResponseFromWeeklyForecastApi response = responseEntity.getBody();

    assert response != null;
    return response.getResponse().getBody().getItems().getItem().get(0);
  }

  private URI makeWeeklyForecastUri(String locationLandCode, LocalDateTime now)
      throws URISyntaxException {

    String requestUri = "http://apis.data.go.kr/1360000/MidFcstInfoService/getMidLandFcst?";
    requestUri += "serviceKey=" + longTermWeatherProperties.getServiceKey();
    requestUri += "&numOfRows=" + longTermWeatherProperties.getNumOfRows();
    requestUri += "&pageNo=1";
    requestUri += "&dataType=" + longTermWeatherProperties.getDataType();
    requestUri += "&regId=" + locationLandCode;
    requestUri += "&tmFc=" + getStandardDateTime(now);
    return new URI(requestUri);

  }

  private WeeklyTemperatureItem getWeeklyTemperatureFromApi(LocalDateTime now,
      String locationCode) throws URISyntaxException {
    URI uri = makeWeeklyTemperatureUri(locationCode, now);

    ResponseEntity<ResponseFromWeeklyTemperatureApi> responseEntity = restTemplate.getForEntity(uri,
        ResponseFromWeeklyTemperatureApi.class);
    ResponseFromWeeklyTemperatureApi response = responseEntity.getBody();

    assert response != null;
    return response.getResponse().getBody().getItems().getItem().get(0);
  }

  private URI makeWeeklyTemperatureUri(String locationCode, LocalDateTime now)
      throws URISyntaxException {

    String requestUri = "http://apis.data.go.kr/1360000/MidFcstInfoService/getMidTa?";
    requestUri += "serviceKey=" + longTermWeatherProperties.getServiceKey();
    requestUri += "&numOfRows=" + longTermWeatherProperties.getNumOfRows();
    requestUri += "&pageNo=1";
    requestUri += "&dataType=" + longTermWeatherProperties.getDataType();
    requestUri += "&regId=" + locationCode;
    requestUri += "&tmFc=" + getStandardDateTime(now);
    return new URI(requestUri);

  }

  private String getStandardDateTime(LocalDateTime now) {
    int nowHour = now.toLocalTime().getHour();
    LocalDate today = now.toLocalDate();
    LocalDateTime standardDateTime;

    if (nowHour < MORNING_STANDARD_TIME.getHour()) {  // 06시 이전에는 그 전날 18시가 기준 발표시간
      standardDateTime = LocalDateTime.of(today.minusDays(1), EVENING_STANDARD_TIME);
    } else if (nowHour < EVENING_STANDARD_TIME.getHour()) {  // 06시 ~ 18시는 06시가 기준 발표시간
      standardDateTime = LocalDateTime.of(today, MORNING_STANDARD_TIME);
    } else {   // 18시 이후에는 18시가 기준 발표시간
      standardDateTime = LocalDateTime.of(today, EVENING_STANDARD_TIME);
    }

    return standardDateTime.format(DateTimeFormatter.ofPattern("yyyyMMddHHmm"));
  }
}
