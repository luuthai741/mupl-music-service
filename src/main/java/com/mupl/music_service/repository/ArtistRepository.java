package com.mupl.music_service.repository;

import com.mupl.music_service.entity.ArtistEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.Set;

@Repository
public interface ArtistRepository extends R2dbcRepository<ArtistEntity, Integer> {
    Flux<ArtistEntity> findByNameIn(Set<String> names);
}
