package com.mupl.music_service.service;

import reactor.core.publisher.Mono;

public interface ArtistSongService {
    Mono<Void> create(Integer artistId, Long songId);
}
