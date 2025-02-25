package com.mupl.music_service.service.impl;

import com.mupl.music_service.dto.request.SongCreateRequest;
import com.mupl.music_service.dto.response.SongResponse;
import com.mupl.music_service.entity.SongEntity;
import com.mupl.music_service.kafka.producer.FileProducer;
import com.mupl.music_service.repository.AlbumRepository;
import com.mupl.music_service.repository.ArtistRepository;
import com.mupl.music_service.repository.GenreRepository;
import com.mupl.music_service.repository.SongRepository;
import com.mupl.music_service.service.ArtistSongService;
import com.mupl.music_service.service.GenreSongService;
import com.mupl.music_service.service.SongService;
import com.mupl.music_service.service.StorageService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
    private final FileProducer fileProducer;
    @Override
    public Mono<SongResponse> createSong(SongCreateRequest songCreateRequest) {
        SongEntity songEntity = modelMapper.map(songCreateRequest, SongEntity.class);
        songEntity.setCreatedAt(LocalDateTime.now());
        songEntity.setUpdatedAt(LocalDateTime.now());
        return songRepository.save(songEntity)
                .flatMap(entity ->{
                    Mono.when(
                            createArtistSong(entity.getSongId(), songCreateRequest.getArtistIds()),
                            createGenreSong(entity.getSongId(), songCreateRequest.getGenreIds()),
                            fileProducer.sendFile(entity.getSongId(), songCreateRequest.getTitle(), songCreateRequest.getImageFile()));
                    return Mono.just(modelMapper.map(songEntity, SongResponse.class));
                });
    }

    private Mono<Void> createArtistSong(Long songId, List<Integer> artistIds) {
        return Flux.fromIterable(artistIds)
                .flatMap(artistId -> artistSongService.create(artistId, songId))
                .then();
    }

    private Mono<Void> createGenreSong(Long songId, List<Integer> genreIds) {
        return Flux.fromIterable(genreIds)
                .flatMap(genreId -> genreSongService.create(genreId, songId))
                .then();
    }
}
