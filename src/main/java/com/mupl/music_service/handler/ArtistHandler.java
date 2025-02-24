package com.mupl.music_service.handler;

import com.mupl.music_service.dto.request.ArtistCreateRequest;
import com.mupl.music_service.dto.response.ArtistResponse;
import com.mupl.music_service.service.ArtistService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
@Slf4j
public class ArtistHandler {
    private final ArtistService artistService;

    public Mono<ServerResponse> createArtist(ServerRequest request) {
        return request.bodyToMono(ArtistCreateRequest.class)
                .flatMap(artistCreateRequest -> {
                    log.info("Create artist : {}, Thread name: {}", artistCreateRequest, Thread.currentThread().getName());
                     return ServerResponse.ok().body(artistService.createArtist(artistCreateRequest), ArtistResponse.class);
                });
    }

    public Mono<ServerResponse> getArtists(ServerRequest request) {
        return ServerResponse.ok().body(artistService.getArtists(), ArtistResponse.class);
    }
}
