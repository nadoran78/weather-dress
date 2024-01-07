package com.fighting.weatherdress.weather.service;

import com.fighting.weatherdress.weather.dto.LocationDto;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CoordinateLocationConvertor {

  @Value("${location.convert.kakao-key}")
  private String apiKey;

  public LocationDto convertCoordinateToLocation(double lat, double lng) {
    String requestUri = "https://dapi.kakao.com/v2/local/geo/coord2address.json?";
    requestUri += "x=" + lng;
    requestUri += "&y=" + lat;

    HttpHeaders headers = new HttpHeaders();
    headers.add("Authorization", apiKey);

    RestTemplate restTemplate = new RestTemplate();
    ResponseEntity<String> response = restTemplate.exchange(requestUri, HttpMethod.GET,
        new HttpEntity<>(headers), String.class);

    String responseBody = response.getBody();

    assert responseBody != null;
    JsonElement element = JsonParser.parseString(responseBody);
    JsonObject object = element.getAsJsonObject();
    JsonArray array = object.getAsJsonArray("documents");
    JsonObject document = array.get(0).getAsJsonObject();
    JsonObject address = document.getAsJsonObject("address");
    JsonElement sido = address.get("region_1depth_name");
    JsonElement sigungu = address.get("region_2depth_name");

    return new LocationDto(sido.getAsString(), sigungu.getAsString());
  }
}
