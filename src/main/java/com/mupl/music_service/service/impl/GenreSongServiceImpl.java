package com.mupl.music_service.service.impl;

import com.mupl.music_service.entity.GenreSongEntity;
import com.mupl.music_service.repository.GenreSongRepository;
import com.mupl.music_service.service.GenreSongService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Transactional
@RequiredArgsConstructor
public class GenreSongServiceImpl implements GenreSongService {
    private final GenreSongRepository genreSongRepository;

    @Override
    public Mono<Void> create(Integer genreId, Long songId) {
        GenreSongEntity genreSongEntity = GenreSongEntity.builder()
                .genreId(genreId)
                .songId(songId)
                .build();
        return genreSongRepository.save(genreSongEntity)
                .then();
    }

    @Override
    public Mono<Void> deleteAllBySongId(Long songId) {
        return genreSongRepository.deleteAllBySongId(songId);
    }
}
