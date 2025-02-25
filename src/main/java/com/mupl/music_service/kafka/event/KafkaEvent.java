package com.mupl.music_service.kafka.event;

import com.mupl.music_service.utils.constain.EventType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KafkaEvent<T> {
    private String eventId;
    private EventType eventType;
    private LocalDateTime eventTime;
    private String source;
    private String correlationId;
    private T payload;
}
