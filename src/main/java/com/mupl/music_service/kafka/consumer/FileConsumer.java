package com.mupl.music_service.kafka.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mupl.music_service.kafka.event.FilePayload;
import com.mupl.music_service.kafka.event.KafkaEvent;
import com.mupl.music_service.service.StorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.objectweb.asm.TypeReference;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class FileConsumer {
    private final StorageService storageService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "file-uploads", groupId = "file-upload-group")
    public void consume(final ConsumerRecord<String, byte[]> record) {
        try {
            KafkaEvent<FilePayload> event = objectMapper.readValue(record.value(), new TypeReference() {});
            FilePayload fileUpload = event.getPayload();
            log.info("Received file payload: {}", fileUpload);
            storageService.uploadToMinio(fileUpload.getSongId(), fileUpload.getFilename(), fileUpload.getData());
        }catch (Exception e) {
            log.error("Error while consuming file uploaded record", e);
        }
    }
}
