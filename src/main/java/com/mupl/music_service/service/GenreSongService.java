package com.mupl.music_service.service;

import reactor.core.publisher.Mono;

public interface GenreSongService {
    Mono<Void> create(Integer genreId, Long songId);
}
