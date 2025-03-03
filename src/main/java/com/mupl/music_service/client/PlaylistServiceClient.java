package com.mupl.music_service.client;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class PlaylistServiceClient {
    private final WebClient webClient;
    @Value("${services.playlist-service}")
    private String playlistServiceUrl;

    public Mono<Void> deletePlaylistBySongId(long songId) {
       return webClient.delete()
               .uri(String.format("%s/api/v1/playlists-service/songs/%s", playlistServiceUrl,songId))
               .retrieve()
               .bodyToMono(Void.class);
    }
}
