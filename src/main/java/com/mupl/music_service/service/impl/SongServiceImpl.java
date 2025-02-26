package com.mupl.music_service.service.impl;

import com.mupl.music_service.dto.request.SongCreateRequest;
import com.mupl.music_service.dto.response.SongResponse;
import com.mupl.music_service.entity.SongEntity;
import com.mupl.music_service.exception.BadRequestException;
import com.mupl.music_service.repository.SongRepository;
import com.mupl.music_service.service.ArtistSongService;
import com.mupl.music_service.service.GenreSongService;
import com.mupl.music_service.service.SongService;
import com.mupl.music_service.service.StorageService;
import com.mupl.music_service.utils.constain.EventType;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class SongServiceImpl implements SongService {
    private final SongRepository songRepository;
    private final ModelMapper modelMapper;
    private final ArtistSongService artistSongService;
    private final GenreSongService genreSongService;
    private final StorageService storageService;

    @Override
    public Mono<SongResponse> createSong(SongCreateRequest songCreateRequest) {
        SongEntity songEntity = modelMapper.map(songCreateRequest, SongEntity.class);
        songEntity.setCreatedAt(LocalDateTime.now());
        songEntity.setUpdatedAt(LocalDateTime.now());
        return songRepository.save(songEntity)
                .flatMap(entity -> Mono.when(
                                createArtistSongRelationship(entity.getSongId(), songCreateRequest.getArtistIds()),
                                createGenreSongRelationship(entity.getSongId(), songCreateRequest.getGenreIds()))
                        .thenReturn(modelMapper.map(songEntity, SongResponse.class))
                        .doOnSuccess(response -> {
                            storageService.uploadFile(response.getSongId(), songCreateRequest.getImageFile(), EventType.IMAGE_UPLOADED)
                                    .subscribeOn(Schedulers.boundedElastic())
                                    .subscribe();
                            storageService.uploadFile(response.getSongId(), songCreateRequest.getSongFile(), EventType.SONG_UPLOADED)
                                    .subscribeOn(Schedulers.boundedElastic())
                                    .subscribe();
                        })
                );
    }

    @Override
    public Mono<SongResponse> getSong(String songId) {
        return songRepository.findById(Long.parseLong(songId))
                .switchIfEmpty(Mono.error(new BadRequestException("Song id not found")))
                .flatMap(entity -> Mono.just(modelMapper.map(entity, SongResponse.class)));
    }

    @Override
    public Mono<SongResponse> deleteSong(String songId) {
        return songRepository.findById(Long.parseLong(songId))
                .switchIfEmpty(Mono.error(new BadRequestException("Song id not found")))
                .flatMap(entity -> songRepository.delete(entity).then(Mono.just(modelMapper.map(entity, SongResponse.class))))
                .doOnSuccess(response -> {
                    storageService.deleteObject(response.getSongPath())
                            .subscribeOn(Schedulers.boundedElastic())
                            .subscribe();
                    storageService.deleteObject(response.getImagePath())
                            .subscribeOn(Schedulers.boundedElastic())
                            .subscribe();
                });
    }

    private Mono<Void> createArtistSongRelationship(Long songId, List<Integer> artistIds) {
        return artistSongService.deleteAllBySongId(songId)
                .then(Mono.defer(() -> Flux.fromIterable(artistIds)
                        .flatMap(artistId -> artistSongService.create(artistId, songId))
                        .then()));
    }

    private Mono<Void> createGenreSongRelationship(Long songId, List<Integer> genreIds) {
        return genreSongService.deleteAllBySongId(songId)
                .then(Mono.defer(() -> Flux.fromIterable(genreIds)
                        .flatMap(genreId -> genreSongService.create(genreId, songId))
                        .then()));
    }
}
