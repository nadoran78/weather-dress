package com.fighting.weatherdress.member.domain.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.Arrays;
import java.util.List;

@Converter
public class RolesConverter implements AttributeConverter<List<String>, String> {

  @Override
  public String convertToDatabaseColumn(List<String> attribute) {
    StringBuilder sb = new StringBuilder();
    for (String s : attribute) {
      sb.append(s).append(" ");
    }
    return sb.toString();
  }

  @Override
  public List<String> convertToEntityAttribute(String dbData) {
    String result = dbData.strip();
    return Arrays.stream(result.split(" ")).toList();
  }
}
