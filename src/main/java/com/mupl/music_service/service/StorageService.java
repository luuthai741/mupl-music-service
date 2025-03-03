package com.mupl.music_service.service;

import com.mupl.music_service.dto.response.SongMetaData;
import com.mupl.music_service.entity.SongEntity;
import com.mupl.music_service.exception.BadRequestException;
import com.mupl.music_service.repository.SongRepository;
import com.mupl.music_service.utils.FileUtils;
import com.mupl.music_service.utils.RequestUtils;
import com.mupl.music_service.utils.constain.EventType;
import io.minio.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
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

    public Mono<String> uploadToMinio(Long songId, String songName, byte[] fileData, EventType eventType) {
        checkBucket();
        String filePath = FileUtils.getFileName(songId, songName);
        log.info("uploadToMinio: songId={}, filePath={}, eventType={}", songId, filePath, eventType);
        return Mono.fromCallable(() -> {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(fileData);
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(BUCKET_NAME)
                            .object(filePath)
                            .stream(inputStream, fileData.length, -1)
                            .contentType(EventType.getContentTypeByEventType(eventType))
                            .build());
            return filePath;
        });
    }

    public Mono<Void> deleteObject(String objectName) {
        if (StringUtils.isBlank(objectName)) {
            return Mono.empty();
        }
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
            log.info("Event {} filePart is empty", eventType);
            return Mono.empty();
        }
        return DataBufferUtils.join(filePart.content())
                .flatMap(dataBuffer -> {
                    try {
                        byte[] bytes = new byte[dataBuffer.readableByteCount()];
                        dataBuffer.read(bytes);
                        return uploadToMinio(songId, filePart.filename(), bytes, eventType)
                                .flatMap(filePath -> songRepository.findById(songId)
                                        .flatMap(song -> {
                                            setBaseOnEventType(eventType, song, filePath, bytes);
                                            log.info("Update song {}, path {}", song, filePath);
                                            return songRepository.save(song).then(Mono.empty());
                                        }));
                    } finally {
                        DataBufferUtils.release(dataBuffer);
                    }
                })
                .onErrorResume(e -> {
                    log.error("Failed to process file upload", e);
                    return Mono.empty(); // Tránh crash ứng dụng
                }).then();
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

    public Mono<SongMetaData> getSongMetaData(String songId) {
        return songRepository.findById(Long.parseLong(songId))
                .flatMap(songEntity -> {
                    try {
                        StatObjectResponse stat = minioClient.statObject(
                                StatObjectArgs.builder()
                                        .bucket(BUCKET_NAME)
                                        .object(songEntity.getSongPath())
                                        .build()
                        );
                        return Mono.just(SongMetaData.builder()
                                .size(stat.size())
                                .songPath(songEntity.getSongPath())
                                .songId(songId)
                                .build());
                    } catch (Exception e) {
                        return Mono.error(new RuntimeException("Error getting song metadata", e));
                    }
                });
    }

    public Mono<ServerResponse> streamSong(ServerRequest request) {
        String rangeHeader = RequestUtils.getRequestHeader(request, "Range");
        String songId = RequestUtils.getPathVariable(request, "id");
        return getSongMetaData(songId)
                .flatMap(songMetaData -> {
                    long startByte = 0;
                    long endByte = songMetaData.getSize() - 1;
                    if (StringUtils.isNotBlank(rangeHeader) && rangeHeader.startsWith("bytes=")) {
                        String[] ranges = rangeHeader.substring(6).split("-");
                        startByte = Long.parseLong(ranges[0]);
                        if (ranges.length > 1 && StringUtils.isNotBlank(ranges[1])) {
                            endByte = Long.parseLong(ranges[1]);
                        }
                    }
                    long contentLength = endByte - startByte + 1;
                    return ServerResponse.status(HttpStatus.PARTIAL_CONTENT.value())
                            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE)
                            .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(contentLength))
                            .header(HttpHeaders.ACCEPT_RANGES, "bytes")
                            .header(HttpHeaders.CONNECTION, "keep-alive")
                            .header(HttpHeaders.CONTENT_RANGE, String.format("bytes %d-%d/%d", startByte, endByte, songMetaData.getSize()))
                            .body(BodyInserters.fromPublisher(streamSong(songId, startByte, endByte), DataBuffer.class));
                });
    }

    public Flux<DataBuffer> getImage(String songId) {
        return songRepository.findById(Long.parseLong(songId))
                .flux()
                .flatMap(songEntity -> {
                    try {
                        InputStream inputStream = minioClient.getObject(
                                GetObjectArgs.builder()
                                        .bucket(BUCKET_NAME)
                                        .object(songEntity.getImagePath())
                                        .build()
                        );
                        return DataBufferUtils.readInputStream(
                                () -> inputStream,
                                new DefaultDataBufferFactory(),
                                2048
                        ).doFinally(signalType -> {
                            try {
                                inputStream.close();
                            } catch (Exception e) {
                                System.out.println("Error closing input stream");
                            }
                        });
                    } catch (Exception e) {
                        return Flux.error(new BadRequestException("Error retrieving image from MinIO"));
                    }
                });
    }

    public Flux<DataBuffer> streamSong(String songId, long start, long end) {
        return songRepository.findById(Long.parseLong(songId))
                .flux()
                .flatMap(song -> {
                    try {
                        InputStream inputStream = minioClient.getObject(
                                GetObjectArgs.builder()
                                        .bucket(BUCKET_NAME)
                                        .object(song.getSongPath())
                                        .offset(start)
                                        .length(end - start)
                                        .build()
                        );
                        return Flux.create(sink -> {
                            DataBufferFactory dataBufferFactory = new DefaultDataBufferFactory();
                            byte[] buffer = new byte[2056];
                            try {
                                int bytesRead;
                                while ((bytesRead = inputStream.read(buffer)) != -1) {
                                    DataBuffer dataBuffer = dataBufferFactory.allocateBuffer(bytesRead);
                                    dataBuffer.write(buffer, 0, bytesRead);
                                    sink.next(dataBuffer);
                                }
                                sink.complete();
                            } catch (IOException e) {
                                sink.error(new RuntimeException("Error streaming file", e));
                            } finally {
                                try {
                                    log.debug("Stopping streaming song {}", songId);
                                    inputStream.close();
                                } catch (IOException e) {
                                    log.warn("Failed to close InputStream", e);
                                }
                            }
                        });
                    } catch (Exception e) {
                        return Flux.error(new RuntimeException("Error fetching file from MinIO", e));
                    }
                });
    }

}
