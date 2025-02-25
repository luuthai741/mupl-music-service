package com.mupl.music_service.service;

import com.mupl.music_service.utils.FileUtils;
import io.minio.*;
import io.minio.errors.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Service
@RequiredArgsConstructor
@Slf4j
public class StorageService {
    private final String BUCKET_NAME = "mupl";

    private final MinioClient minioClient;
    private final DataBufferFactory dataBufferFactory = new DefaultDataBufferFactory();

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
        } catch (Exception e) {
            log.error("Error check bucket", e);
        }
    }

    private Mono<File> convertFilePartToFile(FilePart filePart) {
        return Mono.fromCallable(() -> File.createTempFile("upload_", ".mp3"))
                .flatMap(tempFile -> filePart.content()
                        .reduce(DataBuffer::write)
                        .flatMap(buffer -> {
                            try (FileOutputStream outputStream = new FileOutputStream(tempFile)) {
                                byte[] bytes = new byte[buffer.readableByteCount()];
                                buffer.read(bytes);
                                outputStream.write(bytes);
                                return Mono.just(tempFile);
                            } catch (IOException e) {
                                return Mono.error(new RuntimeException("Error writing file", e));
                            }
                        })
                );
    }

    public Mono<String> uploadToMinio(Long songId, String songName, byte[] fileData) {
        checkBucket();
        final String songPath = FileUtils.getFileName(songName);
        return Mono.fromCallable(() -> {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(fileData);
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(BUCKET_NAME)
                            .object(songPath)
                            .stream(inputStream, fileData.length, -1)
                            .contentType("audio/mpeg")
                            .build());
            return songPath;
        }).subscribeOn(Schedulers.boundedElastic());
    }

    public Mono<String> uploadToMinio(String songName, FilePart filePart) {
        checkBucket();
        final String songPath = FileUtils.getFileName(songName);
        return convertFilePartToFile(filePart)
                .flatMap(file -> {
                    try (FileInputStream fileInputStream = new FileInputStream(file)) {
                        minioClient.putObject(
                                PutObjectArgs.builder()
                                        .bucket(BUCKET_NAME)
                                        .object(songPath)
                                        .stream(fileInputStream, file.length(), -1)
                                        .contentType("audio/mpeg")
                                        .build()
                        );
                    } catch (IOException | ErrorResponseException | InsufficientDataException | InternalException |
                             InvalidKeyException | InvalidResponseException | NoSuchAlgorithmException |
                             ServerException |
                             XmlParserException e) {
                        log.error("Error occurred when uploading song {} to minio", songName, e);
                    }
                    return Mono.just(songPath);
                });
    }

    public Flux<DataBuffer> streamSong(String songName) {
        try {
            InputStream inputStream = minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(BUCKET_NAME)
                            .object(songName)
                            .build()
            );
            return Flux.generate(sink -> {
                try {
                    byte[] buffer = new byte[2056];
                    int bytesRead = inputStream.read(buffer);
                    if (bytesRead == -1) {
                        log.info("No more data available");
                        sink.complete();
                    } else {
                        DataBuffer dataBuffer = dataBufferFactory.allocateBuffer(bytesRead);
                        dataBuffer.write(buffer, 0, bytesRead);
                        log.debug("Read {} bytes", bytesRead);
                        sink.next(dataBuffer);
                    }
                } catch (Exception e) {
                    log.error("Error occurred when streaming song {}", songName, e);
                    sink.error(e);
                }
            });
        } catch (Exception e) {
            return Flux.error(new RuntimeException("Error fetching file from MinIO", e));
        }
    }

}
