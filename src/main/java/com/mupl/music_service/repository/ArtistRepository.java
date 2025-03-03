package com.mupl.music_service.repository;

import com.mupl.music_service.entity.ArtistEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Set;

@Repository
public interface ArtistRepository extends R2dbcRepository<ArtistEntity, Integer> {
    Flux<ArtistEntity> findByNameIn(Set<String> names);
    Flux<ArtistEntity> findAllBy(Pageable pageable);
    Mono<Boolean> existsByName(String name);
    @Query("""
                SELECT a.*
                FROM mupl_artist a
                LEFT JOIN mupl_artists_songs ass ON ass.artist_id = a.artist_id
                LEFT JOIN mupl_song s ON s.song_id = ass.song_id
                WHERE ass.song_id = :songId 
            """)
    Flux<ArtistEntity> findAllBySongId(@Param("songId") long SongId);
}
