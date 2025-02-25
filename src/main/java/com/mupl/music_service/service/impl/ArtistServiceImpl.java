package com.mupl.music_service.service.impl;

import com.mupl.music_service.dto.request.artist.ArtistCreateRequest;
import com.mupl.music_service.dto.request.artist.ArtistUpdateRequest;
import com.mupl.music_service.dto.response.ArtistResponse;
import com.mupl.music_service.dto.response.PageableResponse;
import com.mupl.music_service.entity.ArtistEntity;
import com.mupl.music_service.exception.BadRequestException;
import com.mupl.music_service.repository.ArtistRepository;
import com.mupl.music_service.service.ArtistService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class ArtistServiceImpl implements ArtistService {
    private final ArtistRepository artistRepository;
    private ModelMapper modelMapper;

    @Override
    public Mono<ArtistResponse> getArtist(String id) {
        return artistRepository.findById(Integer.parseInt(id))
                .switchIfEmpty(Mono.error(new BadRequestException("Artist id not found")))
                .flatMap(artist -> Mono.just(modelMapper.map(artist, ArtistResponse.class)));
    }

    @Override
    public Mono<ArtistResponse> createArtist(ArtistCreateRequest request) {
        return artistRepository.existsByName(request.getName())
                .flatMap(exists -> {
                    if (exists) {
                        log.info("Artist {} already exists", request.getName());
                        return Mono.error(new BadRequestException("Artist name already exists"));
                    }
                    log.info("Create artist : {}, Thread name: {}", request, Thread.currentThread().getName());
                    ArtistEntity artistEntity = modelMapper.map(request, ArtistEntity.class);
                    artistEntity.setCreatedAt(LocalDateTime.now());
                    artistEntity.setCountry(request.getCountry().getUrl());
                    artistEntity.setGender(request.getGender().getValue());
                    return artistRepository.save(artistEntity)
                            .map(artist -> modelMapper.map(artist, ArtistResponse.class));
                });
    }

    @Override
    public Mono<ArtistResponse> deleteArtist(String id) {
        return artistRepository.findById(Integer.parseInt(id))
                .switchIfEmpty(Mono.error(new BadRequestException("Artist id not found")))
                .flatMap(artist -> {
                    log.info("Artist {} deleted", artist.getName());
                    return artistRepository.delete(artist)
                            .then(Mono.just(modelMapper.map(artist, ArtistResponse.class)));
                });
    }

    @Override
    public Mono<PageableResponse> getArtists(int page, int size, String sortBy, Sort.Direction sortOrder) {
        Sort sort = Sort.by(sortOrder, sortBy);
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        return artistRepository.findAllBy(pageable)
                .collectList()
                .zipWith(artistRepository.count())
                .map(tuple -> {
                    PageImpl pagable =  new PageImpl(tuple.getT1(), pageable, tuple.getT2());
                    return PageableResponse.builder()
                                    .content(pagable.getContent())
                                    .page(page)
                                    .pageSize(size)
                                    .totalPages(pagable.getTotalPages())
                                    .totalElements(pagable.getTotalElements())
                            .build();
                });
    }

    @Override
    public Mono<ArtistResponse> updateArtist(String id, ArtistUpdateRequest request) {
        return artistRepository.findById(Integer.parseInt(id))
                .switchIfEmpty(Mono.error(new BadRequestException("Artist id not found")))
                .flatMap(artist -> {
                    artist.setGender(request.getGender().getValue());
                    artist.setCountry(request.getCountry().getUrl());
                    artist.setBirthday(request.getBirthday());
                    artist.setDescription(request.getDescription());
                    return artistRepository.save(artist)
                            .flatMap(artistUpdated -> Mono.just(modelMapper.map(artistUpdated, ArtistResponse.class)));
                });
    }
}
