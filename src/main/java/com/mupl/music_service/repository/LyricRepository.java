package com.mupl.music_service.repository;

import com.mupl.music_service.entity.LyricEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Repository
public interface LyricRepository extends R2dbcRepository<LyricEntity, Long> {
    Mono<LyricEntity> findBySongId(Long id);
    Mono<Boolean> existsBySongId(Long id);
    Flux<LyricEntity> findAllBy(Pageable pageable);
}
