package com.mupl.music_service.kafka.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mupl.music_service.kafka.event.KafkaEvent;
import com.mupl.music_service.kafka.event.SongDeletePayload;
import com.mupl.music_service.utils.constain.KafkaEventType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class SongProducer {
    private final KafkaTemplate<String, byte[]> producer;
    private final String SONG_TOPIC = "mupl_song_topic";
    private final ObjectMapper objectMapper;
    @Value("${spring.application.name}")
    private String serviceName;

    public Mono<Void> sendSongDeleteEvent(Long songId) {
        return Mono.defer(() -> {
            SongDeletePayload songDeletePayload = new SongDeletePayload(songId);
            KafkaEvent<SongDeletePayload> event = KafkaEvent.<SongDeletePayload>builder()
                    .eventId(UUID.randomUUID().toString())
                    .correlationId(UUID.randomUUID().toString())
                    .eventTime(LocalDateTime.now())
                    .eventType(KafkaEventType.SONG_DELETED.getEventName())
                    .payload(songDeletePayload)
                    .source(serviceName)
                    .build();
            try {
                byte[] messageBytes = objectMapper.writeValueAsBytes(event);
                String jsonMessage = new String(messageBytes, StandardCharsets.UTF_8);
                log.info("📤 Sending message: {}", jsonMessage);
                producer.send(SONG_TOPIC, String.valueOf(songId), messageBytes);
                log.info("Sent song delete event successfully, songId: {}", songId);
            } catch (Exception e) {
                log.error("Error sending SongDeleteEvent, songId {} {}", songId, e.getMessage());
                return Mono.error(e);
            }
            return Mono.empty();
        });
    }
}
