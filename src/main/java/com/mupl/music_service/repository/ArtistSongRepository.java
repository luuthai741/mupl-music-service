package com.mupl.music_service.repository;

import com.mupl.music_service.entity.ArtistSongEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtistSongRepository extends R2dbcRepository<ArtistSongEntity, ArtistSongEntity.ArtistSongId> {
}
