package com.mupl.music_service.repository;

import com.mupl.music_service.entity.SongEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SongQuantityRepository extends R2dbcRepository<SongEntity, Long> {
}
