package com.mupl.music_service.service;

import com.mupl.music_service.dto.request.SongCreateRequest;
import com.mupl.music_service.entity.SongEntity;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Mono;

public interface SongService {
    Mono<SongEntity> createSong(SongCreateRequest songCreateRequest);
}
