package com.mupl.music_service.service;

import com.mupl.music_service.dto.request.genre.GenreRequest;
import com.mupl.music_service.dto.response.GenreResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface GenreService {
    Mono<GenreResponse> createGenre(GenreRequest genreRequest);
    Mono<GenreResponse> getGenre(String genreId);
    Mono<GenreResponse> deleteGenre(String genreId);
    Flux<GenreResponse> getAllGenres();
}
