package com.mupl.music_service.service;

import com.mupl.music_service.entity.SongEntity;
import com.mupl.music_service.repository.SongRepository;
import com.mupl.music_service.utils.constain.EventType;
import io.minio.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.*;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class StorageService {
    private final String BUCKET_NAME = "mupl";
    private final SongRepository songRepository;
    private final MinioClient minioClient;
    private final DataBufferFactory dataBufferFactory = new DefaultDataBufferFactory();
    private static boolean isBucketExist = false;

    private void checkBucket() {
        if (isBucketExist) {
            return;
        }
        try {
            boolean isExists = minioClient.bucketExists(BucketExistsArgs.builder()
                    .bucket(BUCKET_NAME)
                    .build());
            if (!isExists) {
                minioClient.makeBucket(MakeBucketArgs.builder()
                        .bucket(BUCKET_NAME)
                        .build());
            }
            isBucketExist = true;
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

    public Mono<String> uploadToMinio(String songName, byte[] fileData, EventType eventType) {
        checkBucket();
        return Mono.fromCallable(() -> {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(fileData);
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(BUCKET_NAME)
                            .object(songName)
                            .stream(inputStream, fileData.length, -1)
                            .contentType(EventType.getContentTypeByEventType(eventType))
                            .build());
            return songName;
        });
    }

    public Mono<Void> deleteObject(String objectName) {
        return Mono.fromCallable(() -> {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(BUCKET_NAME)
                            .object(objectName)
                            .build()
            );
            log.info("Successfully deleted object: {}", objectName);
            return null;
        });
    }

    public Mono<Void> uploadFile(Long songId, FilePart filePart, EventType eventType) {
        if (ObjectUtils.isEmpty(filePart)) {
            log.warn("Event {} filePart is empty", eventType);
            return Mono.empty();
        }
        return DataBufferUtils.join(filePart.content())
                .flatMap(dataBuffer -> {
                    byte[] bytes = new byte[dataBuffer.readableByteCount()];
                    dataBuffer.read(bytes);
                    DataBufferUtils.release(dataBuffer);
                    return uploadToMinio(filePart.filename(), bytes, eventType)
                            .flatMap(filePath -> songRepository.findById(songId)
                                    .flatMap(song -> {
                                        setBaseOnEventType(eventType, song, filePath, bytes);
                                        log.info("Update song {}, path {}", song, filePath);
                                        return songRepository.save(song).then(Mono.empty());
                                    }));
                });

    }

    private void setBaseOnEventType(EventType eventType, SongEntity songEntity, String filePath, byte[] fileBytes) {
        switch (eventType) {
            case SONG_UPLOADED -> {
                songEntity.setSongPath(filePath);
                songEntity.setDuration(getDuration(fileBytes));
            }
            case IMAGE_UPLOADED -> songEntity.setImagePath(filePath);
        }
    }

    private int getDuration(byte[] bytes) {
        int duration = 0;
        try {
            File file = File.createTempFile("upload_", ".mp3");
            FileOutputStream outputStream = new FileOutputStream(file);
            outputStream.write(bytes);
            AudioFile audioFile = AudioFileIO.read(file);
            duration = audioFile.getAudioHeader().getTrackLength();
            outputStream.close();
            log.info("Duration {}", duration);
        } catch (Exception e) {
            log.error("Error getting duration", e);
            return duration;
        }
        return duration;
    }

//    private Mono<String> uploadToMinio(String songName, FilePart filePart, EventType eventType) {
//        checkBucket();
//        final String songPath = FileUtils.getFileName(songName);
//        return convertFilePartToFile(filePart)
//                .flatMap(file -> {
//                    try (FileInputStream fileInputStream = new FileInputStream(file)) {
//                        minioClient.putObject(
//                                PutObjectArgs.builder()
//                                        .bucket(BUCKET_NAME)
//                                        .object(songPath)
//                                        .stream(fileInputStream, file.length(), -1)
//                                        .contentType(EventType.getContentTypeByEventType(eventType))
//                                        .build()
//                        );
//                    } catch (IOException | ErrorResponseException | InsufficientDataException | InternalException |
//                             InvalidKeyException | InvalidResponseException | NoSuchAlgorithmException |
//                             ServerException |
//                             XmlParserException e) {
//                        log.error("Error occurred when uploading song {} to minio", songName, e);
//                    }
//                    return Mono.just(songPath);
//                });
//    }

    public Flux<DataBuffer> streamSong(String songId) {
        return songRepository.findById(Long.parseLong(songId))
                .flux()
                .flatMap(song -> {
                    try {
                        InputStream inputStream = minioClient.getObject(
                                GetObjectArgs.builder()
                                        .bucket(BUCKET_NAME)
                                        .object(song.getSongPath())
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
                                log.error("Error occurred when streaming song {}", song.getSongId(), e);
                                sink.error(e);
                            }
                        });
                    } catch (Exception e) {
                        return Flux.error(new RuntimeException("Error fetching file from MinIO", e));
                    }
                });
    }

}
