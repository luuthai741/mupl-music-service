package com.mupl.music_service.service;

import com.mupl.music_service.dto.request.SongCreateRequest;
import com.mupl.music_service.dto.response.SongResponse;
import reactor.core.publisher.Mono;

public interface SongService {
    Mono<SongResponse> createSong(SongCreateRequest songCreateRequest);
    Mono<SongResponse> getSong(String songId);
    Mono<SongResponse> deleteSong(String songId);
}
