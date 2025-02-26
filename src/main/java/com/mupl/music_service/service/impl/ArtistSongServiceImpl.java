package com.mupl.music_service.service.impl;

import com.mupl.music_service.entity.ArtistSongEntity;
import com.mupl.music_service.repository.ArtistSongRepository;
import com.mupl.music_service.service.ArtistSongService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Transactional
@RequiredArgsConstructor
public class ArtistSongServiceImpl implements ArtistSongService {
    private final ArtistSongRepository artistSongRepository;

    @Override
    public Mono<Void> create(Integer artistId, Long songId) {
        ArtistSongEntity artistSongEntity = ArtistSongEntity.builder()
                .artistId(artistId)
                .songId(songId)
                .build();
        return artistSongRepository.save(artistSongEntity)
                .then();
    }

    @Override
    public Mono<Void> deleteAllBySongId(Long songId) {
        return artistSongRepository.deleteAllBySongId(songId);
    }
}
