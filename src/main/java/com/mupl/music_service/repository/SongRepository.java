package com.mupl.music_service.repository;

import com.mupl.music_service.entity.SongEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.List;


@Repository
public interface SongRepository extends R2dbcRepository<SongEntity, Long> {
    Flux<SongEntity> findAllBy(Pageable pageable);

    Flux<SongEntity> findAllByAlbumId(Integer albumId, Pageable pageable);

    @Query("""
                SELECT s.* 
                FROM mupl_song s
                LEFT JOIN mupl_genres_songs gs ON s.song_id = gs.song_id
                LEFT JOIN mupl_genre g ON gs.genre_id = g.genre_id
                WHERE g.genre_id IN (:genres)
            """)
    Flux<SongEntity> findByGenres(@Param("genres") List<Integer> genres, Pageable pageable);

    @Query("""
                SELECT s
                FROM mupl_song s
                LEFT JOIN mupl_artists_songs ass ON s.songId = ass.songId
                LEFT JOIN mupl_artist a ON ass.artistId = a.artistId
                WHERE ass.id IN (:artists)
            """)
    Flux<SongEntity> findByArtists(@Param("artists") List<Integer> artists, Pageable pageable);
}
