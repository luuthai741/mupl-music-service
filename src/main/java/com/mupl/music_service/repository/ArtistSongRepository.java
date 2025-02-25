package com.mupl.music_service.repository;

import com.mupl.music_service.entity.ArtistSongEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface ArtistSongRepository extends R2dbcRepository<ArtistSongEntity, Long> {
    Mono<Void> deleteAllBySongId(Long songId);
}
