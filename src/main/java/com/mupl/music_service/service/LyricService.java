package com.mupl.music_service.service;

import com.mupl.music_service.dto.request.LyricRequest;
import com.mupl.music_service.dto.response.LyricResponse;
import com.mupl.music_service.dto.response.PageableResponse;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Mono;

public interface LyricService {
    Mono<LyricResponse> createLyric(LyricRequest lyricRequest);
    Mono<LyricResponse> updateLyric(String lyricId,LyricRequest lyricRequest);
    Mono<PageableResponse> getLyrics(Pageable pageable);
    Mono<LyricResponse> getLyric(String lyricId);
    Mono<LyricResponse> getLyricBySongId(String songId);
    Mono<LyricResponse> deleteLyric(String lyricId);
}
