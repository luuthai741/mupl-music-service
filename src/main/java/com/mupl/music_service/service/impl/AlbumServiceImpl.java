package com.mupl.music_service.service.impl;

import com.mupl.music_service.dto.request.album.AlbumCreateRequest;
import com.mupl.music_service.dto.response.AlbumResponse;
import com.mupl.music_service.dto.response.PageableResponse;
import com.mupl.music_service.entity.AlbumEntity;
import com.mupl.music_service.exception.BadRequestException;
import com.mupl.music_service.repository.AlbumRepository;
import com.mupl.music_service.service.AlbumService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class AlbumServiceImpl implements AlbumService {
    private final AlbumRepository albumRepository;
    private final ModelMapper modelMapper;

    @Override
    public Mono<AlbumResponse> createAlbum(AlbumCreateRequest request) {
        return albumRepository.findByName(request.getName())
                .flatMap(album -> Mono.error(new BadRequestException(album.getName() + " already exists!")))
                .switchIfEmpty(Mono.defer(() -> {
                    AlbumEntity albumEntity = modelMapper.map(request, AlbumEntity.class);
                    albumEntity.setCreatedAt(LocalDateTime.now());
                    return albumRepository.save(albumEntity)
                            .map(entity -> modelMapper.map(entity, AlbumResponse.class));
                })).cast(AlbumResponse.class);
    }

    @Override
    public Mono<AlbumResponse> getAlbum(String id) {
        return albumRepository.findById(Integer.parseInt(id))
                .switchIfEmpty(Mono.error(new BadRequestException("Album not found!")))
                .map(entity -> modelMapper.map(entity, AlbumResponse.class));
    }

    @Override
    public Mono<AlbumResponse> updateAlbum(String id, AlbumCreateRequest request) {
        return albumRepository.findById(Integer.parseInt(id))
                .switchIfEmpty(Mono.error(new BadRequestException("Album not found!")))
                .flatMap(albumEntity -> {
                    albumEntity.setName(request.getName());
                    albumEntity.setArtistId(request.getArtistId());
                    albumEntity.setReleasedAt(request.getReleasedAt());
                    return albumRepository.save(albumEntity)
                            .map(entity -> modelMapper.map(entity, AlbumResponse.class));
                });
    }

    @Override
    public Mono<AlbumResponse> deleteAlbum(String id) {
        return albumRepository.findById(Integer.parseInt(id))
                .switchIfEmpty(Mono.error(new BadRequestException("Album not found!")))
                .flatMap(albumEntity -> albumRepository.delete(albumEntity).then(Mono.just(modelMapper.map(albumEntity, AlbumResponse.class))));
    }

    @Override
    public Mono<PageableResponse> getAlbums(Pageable pageable, String artistId) {
        Flux<AlbumEntity> albumEntityFlux;
        if (StringUtils.isNotBlank(artistId)) {
            albumEntityFlux = albumRepository.findAllByArtistId(Integer.parseInt(artistId), pageable);
        } else {
            albumEntityFlux = albumRepository.findAllBy(pageable);
        }
        return albumEntityFlux
                .map(albumEntity -> modelMapper.map(albumEntity, AlbumResponse.class))
                .collectList()
                .zipWith(albumRepository.count())
                .map(tuple -> new PageableResponse(tuple.getT1(), pageable, tuple.getT2()));
    }
}
