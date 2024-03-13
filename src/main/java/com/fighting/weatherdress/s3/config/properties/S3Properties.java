package com.fighting.weatherdress.s3.config.properties;

import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "cloud.aws")
@Component
@Getter
@Setter
public class S3Properties {
  private Map<String, String> s3;
  private Map<String, String> credentials;
  private Map<String, String> region;
}
