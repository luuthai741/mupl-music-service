package com.mupl.music_service.handler;

import com.mupl.music_service.dto.request.SongCreateRequest;
import com.mupl.music_service.entity.SongEntity;
import com.mupl.music_service.service.SongService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
public class SongHandler {
    private final SongService songService;

    public Mono<ServerResponse> createSong(ServerRequest request) {
        return request.bodyToMono(SongCreateRequest.class)
                .flatMap(songCreateRequest -> {
                    Mono<SongEntity> songEntityMono = songService.createSong(songCreateRequest);
                    return ServerResponse.ok().body(songEntityMono, SongEntity.class);
                });
    }

    public Mono<ServerResponse> getSong(ServerRequest request) {
        return null;
    }
}
