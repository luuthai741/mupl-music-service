package com.mupl.music_service.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mupl.music_service.dto.request.SongCreateRequest;
import com.mupl.music_service.entity.SongEntity;
import com.mupl.music_service.repository.AlbumRepository;
import com.mupl.music_service.repository.ArtistRepository;
import com.mupl.music_service.repository.GenreRepository;
import com.mupl.music_service.repository.SongRepository;
import com.mupl.music_service.service.SongService;
import com.mupl.music_service.service.StorageService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class SongServiceImpl implements SongService {
    private final SongRepository songRepository;
    private final ArtistRepository artistRepository;
    private final AlbumRepository albumRepository;
    private final GenreRepository genreRepository;
    private final StorageService storageService;
    private final ObjectMapper objectMapper;

    @Override
    public Mono<SongEntity> createSong(SongCreateRequest songCreateRequest) {
//        AlbumEntity albumEntity = albumRepository
//                .findByName(songCreateRequest.getAlbum())
//                .orElse(null);
//        GenreEntity genreEntity = genreRepository
//                .findByName(songCreateRequest.getGenre())
//                .orElse(null);
//        SongEntity songEntity = SongEntity.builder()
//                .title(songCreateRequest.getTitle())
//                .artists(artistEntities)
//                .albumEntity(albumEntity)
//                .genreEntity(genreEntity)
//                .releasedAt(songCreateRequest.getReleasedAt())
//                .createdAt(LocalDateTime.now())
//                .updatedAt(LocalDateTime.now())
//                .build();
//        try {
//            storageService.uploadToMinio(songCreateRequest.getTitle(), FileUtils.convertMultipartFileToFile(songCreateRequest.getFile()));
//        } catch (IOException e) {
//            log.error("Error occurred while uploading file", e);
//        }
//        return Mono.just(songEntity);
        return null;
    }

}
