package com.mupl.music_service.service;

import com.mupl.music_service.dto.request.SongRequest;
import com.mupl.music_service.dto.response.PageableResponse;
import com.mupl.music_service.dto.response.SongResponse;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

public interface SongService {
    Mono<SongResponse> createSong(SongRequest songRequest);
    Mono<SongResponse> getSong(String songId);
    Mono<SongResponse> deleteSong(String songId);
    Mono<PageableResponse> getSongs(Pageable pageable, String albumId);
    Mono<PageableResponse> getSongs(Pageable pageable, List<String> ids);
    Mono<SongResponse> updateSong(String songId, SongRequest songRequest);
    Mono<Map<String,Object>> getSongInfo(String songId);
}
