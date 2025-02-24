package com.mupl.music_service.service;

import com.mupl.music_service.dto.request.ArtistCreateRequest;
import com.mupl.music_service.dto.response.ArtistResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ArtistService {
    Mono<ArtistResponse> createArtist(ArtistCreateRequest request);
    Flux<ArtistResponse> getArtists();
}
