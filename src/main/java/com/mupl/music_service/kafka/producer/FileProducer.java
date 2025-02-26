package com.mupl.music_service.kafka.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mupl.music_service.kafka.event.FilePayload;
import com.mupl.music_service.kafka.event.KafkaEvent;
import com.mupl.music_service.utils.constain.EventType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class FileProducer {
    private final KafkaTemplate<String, byte[]> producer;
    private final String TOPIC = "file-upload";
    private final ObjectMapper objectMapper;
    @Value("${spring.application.name}")
    private String serviceName;

    public Mono<Void> sendFile(Long songId, String songName, FilePart filePart) {
        return DataBufferUtils.join(filePart.content())
                .flatMap(dataBuffer -> {
                    byte[] bytes = new byte[dataBuffer.readableByteCount()];
                    dataBuffer.read(bytes);
                    DataBufferUtils.release(dataBuffer);
                    FilePayload filePayload = FilePayload.builder()
                            .filename(songName)
                            .songId(songId)
                            .contentType(filePart.headers().getContentType().toString())
                            .data(bytes)
                            .build();
                    KafkaEvent<FilePayload> event = KafkaEvent.<FilePayload>builder()
                            .eventId(UUID.randomUUID().toString())
                            .correlationId(UUID.randomUUID().toString())
                            .eventTime(LocalDateTime.now())
                            .eventType(EventType.SONG_UPLOADED)
                            .payload(filePayload)
                            .source(serviceName)
                            .build();
                    try {
                        byte[] messageBytes = objectMapper.writeValueAsBytes(event);
                        return Mono.fromFuture(producer.send(TOPIC, String.valueOf(songId), messageBytes)).then();
                    } catch (Exception e) {
                        log.error("Error while sending file {}", e.getMessage());
                        return Mono.error(e);
                    }
                });
    }
}
