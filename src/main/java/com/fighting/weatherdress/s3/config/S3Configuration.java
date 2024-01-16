package com.fighting.weatherdress.s3.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.fighting.weatherdress.s3.config.properties.S3Properties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class S3Configuration {

  private final S3Properties s3Properties;

  @Bean
  public AmazonS3Client amazonS3Client() {
    BasicAWSCredentials awsCredentials = new BasicAWSCredentials(
        s3Properties.getCredentials().get("accessKey"),
        s3Properties.getCredentials().get("secretKey"));

    return (AmazonS3Client) AmazonS3ClientBuilder.standard()
        .withRegion(s3Properties.getRegion().get("static"))
        .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
        .build();
  }
}
