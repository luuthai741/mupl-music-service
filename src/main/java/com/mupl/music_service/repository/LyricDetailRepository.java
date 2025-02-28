package com.mupl.music_service.repository;

import com.mupl.music_service.entity.LyricDetailEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface LyricDetailRepository extends R2dbcRepository<LyricDetailEntity, Long> {
    Flux<LyricDetailEntity> findAllByLyricId(Long lyricId);
    Mono<Void> deleteByLyricId(Long lyricId);
}
