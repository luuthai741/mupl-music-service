package com.mupl.music_service.handler;

import com.mupl.music_service.dto.request.SongCreateRequest;
import com.mupl.music_service.dto.response.ErrorResponse;
import com.mupl.music_service.dto.response.SongResponse;
import com.mupl.music_service.exception.BadRequestException;
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
                .map(songResponse -> ResponseEntity.status(HttpStatus.CREATED).body(songResponse))
                .onErrorResume(IOException.class, e ->
                        Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body(null))
                );
    }

    public Mono<ServerResponse> streamSong(ServerRequest request) {
        String songId = request.pathVariable("id");
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(BodyInserters.fromPublisher(storageService.streamSong(songId), DataBuffer.class));
    }

    public Mono<ServerResponse> getSong(ServerRequest request) {
        String songId = request.pathVariable("id");
        return songService.getSong(songId)
                .flatMap(songResponse -> ServerResponse.ok().bodyValue(songResponse))
                .onErrorResume(BadRequestException.class, e -> ServerResponse.badRequest().bodyValue(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage())));
    }

    public Mono<ServerResponse> deleteSong(ServerRequest request) {
        String songId = request.pathVariable("id");
        return songService.deleteSong(songId)
                .flatMap(songResponse -> ServerResponse.ok().bodyValue(songResponse))
                .onErrorResume(BadRequestException.class, e -> ServerResponse.badRequest().bodyValue(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage())));
    }
}
