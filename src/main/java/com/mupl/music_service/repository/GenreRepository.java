package com.mupl.music_service.repository;

import com.mupl.music_service.entity.GenreEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Repository
public interface GenreRepository extends R2dbcRepository<GenreEntity, Integer> {
    Mono<GenreEntity> findByName(String name);
}
