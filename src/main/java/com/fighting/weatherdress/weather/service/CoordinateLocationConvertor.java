package com.fighting.weatherdress.weather.service;

import com.fighting.weatherdress.weather.dto.LocationDto;
import com.fighting.weatherdress.weather.dto.api.CoordinateLocationConvertResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class CoordinateLocationConvertor {

  private final RestTemplate restTemplate;

  @Value("${location.convert.kakao-key}")
  private String apiKey;

  public LocationDto convertCoordinateToLocation(double lat, double lng) {
    String requestUri = "https://dapi.kakao.com/v2/local/geo/coord2address.json?";
    requestUri += "x=" + lng;
    requestUri += "&y=" + lat;

    HttpHeaders headers = new HttpHeaders();
    headers.add("Authorization", apiKey);

    ResponseEntity<CoordinateLocationConvertResponse> response = restTemplate.exchange(requestUri,
        HttpMethod.GET, new HttpEntity<>(headers), CoordinateLocationConvertResponse.class);

    CoordinateLocationConvertResponse responseBody = response.getBody();

    assert responseBody != null;
    String sido = responseBody.getDocuments().get(0).getAddress().getRegion1DepthName();
    String sigungu = responseBody.getDocuments().get(0).getAddress().getRegion2DepthName();

    return new LocationDto(sido, sigungu);
  }
}
