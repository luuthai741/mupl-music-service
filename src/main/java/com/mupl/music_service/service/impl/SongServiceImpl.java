package com.mupl.music_service.service.impl;

import com.mupl.music_service.dto.request.SongRequest;
import com.mupl.music_service.dto.response.PageableResponse;
import com.mupl.music_service.dto.response.SongResponse;
import com.mupl.music_service.entity.SongEntity;
import com.mupl.music_service.exception.BadRequestException;
import com.mupl.music_service.kafka.producer.SongProducer;
import com.mupl.music_service.repository.ArtistRepository;
import com.mupl.music_service.repository.SongRepository;
import com.mupl.music_service.service.ArtistSongService;
import com.mupl.music_service.service.GenreSongService;
import com.mupl.music_service.service.SongService;
import com.mupl.music_service.service.StorageService;
import com.mupl.music_service.utils.constain.FileUploadType;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private final ArtistRepository artistRepository;
    private final SongProducer songProducer;

    @Override
    public Mono<SongResponse> createSong(SongRequest songRequest) {
        SongEntity songEntity = modelMapper.map(songRequest, SongEntity.class);
        songEntity.setCreatedAt(LocalDateTime.now());
        songEntity.setUpdatedAt(LocalDateTime.now());
        return songRepository.save(songEntity)
                .flatMap(entity -> Mono.when(
                                createArtistSongRelationship(entity.getSongId(), songRequest.getArtistIds()),
                                createGenreSongRelationship(entity.getSongId(), songRequest.getGenreIds()))
                        .thenReturn(modelMapper.map(songEntity, SongResponse.class))
                        .doOnSuccess(response -> {
                            storageService.uploadFile(response.getSongId(), songRequest.getImageFile(), FileUploadType.IMAGE_UPLOADED)
                                    .subscribeOn(Schedulers.boundedElastic())
                                    .subscribe();
                            storageService.uploadFile(response.getSongId(), songRequest.getSongFile(), FileUploadType.SONG_UPLOADED)
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
        long id = Long.parseLong(songId);
        return songRepository.findById(id)
                .switchIfEmpty(Mono.error(new BadRequestException("Song id not found")))
                .flatMap(entity -> songRepository.delete(entity).then(Mono.just(modelMapper.map(entity, SongResponse.class))))
                .doOnSuccess(response -> {
                    storageService.deleteObject(response.getSongPath())
                            .subscribeOn(Schedulers.boundedElastic())
                            .subscribe();
                    storageService.deleteObject(response.getImagePath())
                            .subscribeOn(Schedulers.boundedElastic())
                            .subscribe();
                    // todo: duplicating code
                    genreSongService.deleteAllBySongId(id)
                            .subscribeOn(Schedulers.boundedElastic())
                            .subscribe();
                    artistSongService.deleteAllBySongId(id)
                            .subscribeOn(Schedulers.boundedElastic())
                            .subscribe();
                    songProducer.sendSongDeleteEvent(id)
                            .subscribeOn(Schedulers.boundedElastic())
                            .subscribe();
                });
    }

    @Override
    public Mono<PageableResponse> getSongs(Pageable pageable, String albumId) {
        Flux<SongEntity> flux;
        if (StringUtils.isNotBlank(albumId)) {
            flux = songRepository.findAllByAlbumId(Integer.parseInt(albumId), pageable);
        } else {
            flux = songRepository.findAllBy(pageable);
        }
        return flux
                .map(song -> modelMapper.map(song, SongResponse.class))
                .collectList()
                .zipWith(songRepository.count())
                .map(tuple -> new PageableResponse(tuple.getT1(), pageable, tuple.getT2()));
    }

    @Override
    public Mono<PageableResponse> getSongs(Pageable pageable, List<String> ids) {
        List<Integer> mapIds = ids.stream().map(Integer::parseInt).toList();
        return songRepository.findByGenres(mapIds, pageable)
                .map(song -> modelMapper.map(song, SongResponse.class))
                .collectList()
                .zipWith(songRepository.count())
                .map(tuple -> new PageableResponse(tuple.getT1(), pageable, tuple.getT2()));

    }

    @Override
    public Mono<SongResponse> updateSong(String songId, SongRequest songRequest) {
        long id = Long.parseLong(songId);
        return songRepository.findById(id)
                .flatMap(entity -> {
                    entity.setReleasedAt(songRequest.getReleasedAt());
                    entity.setTitle(songRequest.getTitle());
                    entity.setAlbumId(songRequest.getAlbumId());
                    entity.setIsFreeToPlay(songRequest.getIsFreeToPlay());
                    return songRepository.save(entity)
                            .map(song -> modelMapper.map(song, SongResponse.class));
                })
                .doOnSuccess(response -> {
                    storageService.deleteObject(response.getImagePath())
                            .then(Mono.defer(() -> storageService.uploadFile(id, songRequest.getImageFile(), FileUploadType.IMAGE_UPLOADED)))
                            .subscribeOn(Schedulers.boundedElastic())
                            .subscribe();
                    storageService.deleteObject(response.getSongPath())
                            .then(Mono.defer(() -> storageService.uploadFile(id, songRequest.getSongFile(), FileUploadType.SONG_UPLOADED)))
                            .subscribeOn(Schedulers.boundedElastic())
                            .subscribe();
                    createArtistSongRelationship(id, songRequest.getArtistIds())
                            .subscribeOn(Schedulers.boundedElastic())
                            .subscribe();
                    createGenreSongRelationship(id, songRequest.getGenreIds())
                            .subscribeOn(Schedulers.boundedElastic())
                            .subscribe();
                });
    }

    @Override
    public Mono<Map<String, Object>> getSongInfo(String songId) {
        long id = Long.parseLong(songId);
        return songRepository.findById(id)
                .switchIfEmpty(Mono.error(new BadRequestException("Song id not found")))
                .flatMap(songEntity -> artistRepository.findAllBySongId(id)
                        .collectList()
                        .flatMap(artistEntities -> {
                            Map<String, Object> map = new HashMap<>();
                            map.put("artists", artistEntities);
                            map.put("songName", songEntity.getTitle());
                            map.put("songId", id);
                            return Mono.just(map);
                        }));
    }

    private Mono<Void> createArtistSongRelationship(Long songId, List<Integer> artistIds) {
        if (CollectionUtils.isEmpty(artistIds)) {
            return Mono.empty();
        }
        return artistSongService.deleteAllBySongId(songId)
                .then(Mono.defer(() -> Flux.fromIterable(artistIds)
                        .flatMap(artistId -> artistSongService.create(artistId, songId))
                        .then()));
    }

    private Mono<Void> createGenreSongRelationship(Long songId, List<Integer> genreIds) {
        if (CollectionUtils.isEmpty(genreIds)) {
            return Mono.empty();
        }
        return genreSongService.deleteAllBySongId(songId)
                .then(Mono.defer(() -> Flux.fromIterable(genreIds)
                        .flatMap(genreId -> genreSongService.create(genreId, songId))
                        .then()));
    }
}
