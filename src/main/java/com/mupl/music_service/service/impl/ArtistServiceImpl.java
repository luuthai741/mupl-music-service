package com.mupl.music_service.service.impl;

import com.mupl.music_service.dto.request.ArtistCreateRequest;
import com.mupl.music_service.dto.response.ArtistResponse;
import com.mupl.music_service.entity.ArtistEntity;
import com.mupl.music_service.repository.ArtistRepository;
import com.mupl.music_service.service.ArtistService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
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
    public Mono<ArtistResponse> createArtist(ArtistCreateRequest request) {
        log.info("Create artist : {}, Thread name: {}", request, Thread.currentThread().getName());
        ArtistEntity artistEntity = modelMapper.map(request, ArtistEntity.class);
        artistEntity.setCreatedAt(LocalDateTime.now());
        artistEntity.setCountry(request.getCountry().getUrl());
        artistEntity.setGender(request.getGender().getValue());
        return artistRepository.save(artistEntity)
                .map(artist ->
                        modelMapper.map(artist, ArtistResponse.class));
    }

    @Override
    public Flux<ArtistResponse> getArtists() {
        return artistRepository.findAll()
                .map(artist -> modelMapper.map(artist, ArtistResponse.class));
    }
}
