package com.mupl.music_service.repository;

import com.mupl.music_service.entity.ArtistSongEntity;
import com.mupl.music_service.entity.GenreSongEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GenreSongRepository extends R2dbcRepository<GenreSongEntity, GenreSongEntity.GenreSongId> {
}
