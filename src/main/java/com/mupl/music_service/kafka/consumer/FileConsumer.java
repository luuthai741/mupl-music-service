package com.mupl.music_service.kafka.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mupl.music_service.repository.SongRepository;
import com.mupl.music_service.service.StorageService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
@Transactional
public class FileConsumer {
    private final StorageService storageService;
    private final ObjectMapper objectMapper;
    private final SongRepository songRepository;

//    @KafkaListener(topics = "file-upload", groupId = "file-upload-group")
//    public void consume(final ConsumerRecord<String, byte[]> record) {
//        try {
//            KafkaEvent<FilePayload> event = objectMapper.readValue(record.value(), KafkaEvent.class);
//            FilePayload filePayload = objectMapper.convertValue(event.getPayload(), FilePayload.class);
//            log.info("Received file payload: {}", filePayload);
//            storageService.uploadToMinio(filePayload.getSongId(), filePayload.getFilename(), filePayload.getData())
//                    .flatMap(filePath -> songRepository.findById(filePayload.getSongId())
//                            .flatMap(song ->{
//                                log.info("Found song: {}, filePath {}", song, filePath);
//                                song.setImagePath(filePath);
//                                return Mono.just(songRepository.save(song));
//                            }))
//                    .subscribeOn(Schedulers.boundedElastic())
//                    .subscribe();
//        } catch (Exception e) {
//            log.error("Error while consuming file uploaded record", e);
//        }
//    }
}
