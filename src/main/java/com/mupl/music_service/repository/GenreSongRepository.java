package com.mupl.music_service.repository;

import com.mupl.music_service.entity.GenreSongEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface GenreSongRepository extends R2dbcRepository<GenreSongEntity, Long> {
    Mono<Void> deleteAllBySongId(Long songId);

}
