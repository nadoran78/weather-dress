package com.fighting.weatherdress.s3.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.fighting.weatherdress.global.exception.CustomException;
import com.fighting.weatherdress.global.type.ErrorCode;
import com.fighting.weatherdress.post.entity.Image;
import com.fighting.weatherdress.s3.config.properties.S3Properties;
import com.fighting.weatherdress.s3.dto.S3FileDto;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class S3FileService {

  private final AmazonS3Client amazonS3Client;
  private final S3Properties properties;

  public List<S3FileDto> saveFile(List<MultipartFile> multipartFiles) {
    List<S3FileDto> fileDtoList = new ArrayList<>();

    List<CompletableFuture<Void>> uploadJobs = multipartFiles.stream()
        .map(multipartFile -> {
          ObjectMetadata objectMetadata = new ObjectMetadata();
          objectMetadata.setContentType(multipartFile.getContentType());
          objectMetadata.setContentLength(multipartFile.getSize());

          String extension = StringUtils.getFilenameExtension(
              multipartFile.getOriginalFilename());
          String filename = UUID.randomUUID() + "." + extension;

          return CompletableFuture.runAsync(() -> {
            try {
              PutObjectRequest putObjectRequest = new PutObjectRequest(
                  properties.getS3().get("bucket"), filename,
                  multipartFile.getInputStream(),
                  objectMetadata);

              amazonS3Client.putObject(putObjectRequest);

              String objectUrl =
                  "https://s3.amazonaws.com/" + properties.getS3().get("bucket") + "/"
                      + filename;
              fileDtoList.add(new S3FileDto(objectUrl, filename));
            } catch (IOException e) {
              throw new CustomException(ErrorCode.FAIL_TO_UPLOAD_FILE);
            }
          });
        })
        .toList();

    CompletableFuture.allOf(uploadJobs.toArray(new CompletableFuture[0])).join();

    return fileDtoList;
  }

  public void deleteImages(List<Image> images) {
    for (Image image : images) {
      amazonS3Client.deleteObject(properties.getS3().get("bucket"),
          StringUtils.getFilename(image.getUrl()));
    }
  }
}
