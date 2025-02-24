package com.mupl.music_service.repository;

import com.mupl.music_service.entity.AlbumEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AlbumRepository extends R2dbcRepository<AlbumEntity, Integer> {
    Optional<AlbumEntity> findByName(String title);

}
