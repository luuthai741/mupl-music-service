package com.mupl.music_service.handler;

import com.mupl.music_service.dto.request.SongRequest;
import com.mupl.music_service.dto.response.ErrorResponse;
import com.mupl.music_service.dto.response.SongResponse;
import com.mupl.music_service.exception.BadRequestException;
import com.mupl.music_service.service.SongService;
import com.mupl.music_service.service.StorageService;
import com.mupl.music_service.utils.RequestUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j
public class SongHandler {
    private final SongService songService;
    private final StorageService storageService;

    @PostMapping(value = "/api/v1/mupl/songs", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<ResponseEntity<SongResponse>> createSong(@ModelAttribute SongRequest songRequest) {
        return songService.createSong(songRequest)
                .map(songResponse -> ResponseEntity.status(HttpStatus.CREATED).body(songResponse))
                .onErrorResume(IOException.class, e ->
                        Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body(null))
                );
    }

    @PutMapping(value = "/api/v1/mupl/songs/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<ResponseEntity<SongResponse>> updateSong(@PathVariable String id, @ModelAttribute SongRequest songRequest) {
        return songService.updateSong(id, songRequest)
                .map(songResponse -> ResponseEntity.ok().body(songResponse))
                .onErrorResume(IOException.class, e ->
                        Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body(null))
                )
                .onErrorResume(BadRequestException.class, e -> Mono.just(ResponseEntity.badRequest().body(null)));
    }

    public Mono<ServerResponse> streamSong(ServerRequest request) {
        // Lấy Range header từ request (nếu có)
        return storageService.streamSong(request);
    }

    public Mono<ServerResponse> getSong(ServerRequest request) {
        return songService.getSong(RequestUtils.getPathVariable(request, "id"))
                .flatMap(songResponse -> ServerResponse.ok().bodyValue(songResponse))
                .onErrorResume(BadRequestException.class, e -> ServerResponse.badRequest().bodyValue(new ErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage())));
    }

    public Mono<ServerResponse> getSongImage(ServerRequest request) {
        String id = RequestUtils.getPathVariable(request, "id");
        Flux<DataBuffer> imageStream = storageService.getImage(id)
                .doOnError(error -> System.err.println("Error streaming image: " + error.getMessage()));
        return ServerResponse.ok()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.IMAGE_JPEG_VALUE) // Hoặc IMAGE_PNG_VALUE nếu cần
                .body(imageStream, DataBuffer.class)
                .onErrorResume(e -> ServerResponse.notFound().build());
    }

    @SuppressWarnings("unchecked")
    public Mono<ServerResponse> getSongs(ServerRequest request) {
        Pageable pageable = RequestUtils.getPageable(request, "songId");
        String albumId = RequestUtils.getPathVariable(request, "albumId");
        List<String> ids = RequestUtils.convertRequestParam(request, List.class, "genres", new ArrayList<>());
        if (CollectionUtils.isEmpty(ids)) {
            return songService.getSongs(pageable, albumId)
                    .flatMap(songResponse -> ServerResponse.ok().bodyValue(songResponse));
        }
        return songService.getSongs(pageable, ids)
                .flatMap(songResponse -> ServerResponse.ok().bodyValue(songResponse));
    }

    public Mono<ServerResponse> deleteSong(ServerRequest request) {
        return songService.deleteSong(RequestUtils.getPathVariable(request, "id"))
                .flatMap(songResponse -> ServerResponse.ok().bodyValue(songResponse))
                .onErrorResume(BadRequestException.class, e -> ServerResponse.badRequest().bodyValue(new ErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage())));
    }
}
