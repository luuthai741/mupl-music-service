package com.mupl.music_service.repository;

import com.mupl.music_service.entity.AlbumEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface AlbumRepository extends R2dbcRepository<AlbumEntity, Integer> {
    Mono<AlbumEntity> findByName(String title);
    Flux<AlbumEntity> findAllBy(Pageable pageable);
}