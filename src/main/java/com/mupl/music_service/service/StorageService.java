package com.mupl.music_service.service;

import com.mupl.music_service.utils.FileUtils;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Service
@RequiredArgsConstructor
@Slf4j
public class StorageService {
    private final String BUCKET_NAME = "mupl";

    private final MinioClient minioClient;

    private void checkBucket() {
       try {
           boolean isExists = minioClient.bucketExists(BucketExistsArgs.builder()
                   .bucket(BUCKET_NAME)
                   .build());
           if (!isExists) {
               minioClient.makeBucket(MakeBucketArgs.builder()
                               .bucket(BUCKET_NAME)
                       .build());
           }
       }catch (Exception e) {
           log.error("Error check bucket", e);
       }
    }

    public void uploadToMinio(String songName, File file) {
        checkBucket();
        songName = FileUtils.getFileName(songName);
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(BUCKET_NAME)
                            .object(songName)
                            .stream(fileInputStream, file.length(), -1)
                            .contentType("audio/mpeg")
                            .build()
            );
        } catch (IOException | ErrorResponseException | InsufficientDataException | InternalException |
                 InvalidKeyException | InvalidResponseException | NoSuchAlgorithmException | ServerException |
                 XmlParserException e) {
            log.error("Error occurred when uploading song {} to minio", songName, e);
        }
    }


}
