package com.mupl.music_service.repository;

import com.mupl.music_service.entity.ArtistEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Set;

@Repository
public interface ArtistRepository extends R2dbcRepository<ArtistEntity, Integer> {
    Flux<ArtistEntity> findByNameIn(Set<String> names);
    Flux<ArtistEntity> findAllBy(Pageable pageable);
    Mono<Boolean> existsByName(String name);
}
