package com.mupl.music_service.handler;

import com.mupl.music_service.dto.request.SongCreateRequest;
import com.mupl.music_service.dto.response.SongResponse;
import com.mupl.music_service.service.SongService;
import com.mupl.music_service.service.StorageService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.io.IOException;

@RestController
@AllArgsConstructor
public class SongHandler {
    private final SongService songService;
    private final StorageService storageService;
    @PostMapping(value = "/api/v1/mupl/songs", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<ResponseEntity<SongResponse>> createSong(@ModelAttribute SongCreateRequest songCreateRequest) {
        return songService.createSong(songCreateRequest)
                .map(ResponseEntity::ok)
                .onErrorResume(IOException.class, e ->
                        Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body(null))
                );
    }

    public Mono<ServerResponse> getSong(ServerRequest request) {
        String songName = request.queryParam("songName").orElse("");
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(BodyInserters.fromPublisher(storageService.streamSong(songName), DataBuffer.class));
    }
}
