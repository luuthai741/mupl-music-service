package com.mupl.music_service.repository;

import com.mupl.music_service.entity.LyricEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LyricRepository extends R2dbcRepository<LyricEntity, Long> {
}
