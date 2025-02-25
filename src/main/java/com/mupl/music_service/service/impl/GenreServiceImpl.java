package com.mupl.music_service.service.impl;

import com.mupl.music_service.dto.request.genre.GenreRequest;
import com.mupl.music_service.dto.response.GenreResponse;
import com.mupl.music_service.entity.GenreEntity;
import com.mupl.music_service.exception.BadRequestException;
import com.mupl.music_service.repository.GenreRepository;
import com.mupl.music_service.service.GenreService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
@Transactional
public class GenreServiceImpl implements GenreService {
    private final GenreRepository genreRepository;
    private final ModelMapper modelMapper;

    @Override
    public Mono<GenreResponse> createGenre(GenreRequest genreRequest) {
        return genreRepository.findByName(genreRequest.getName())
                .flatMap(genre -> Mono.error(new BadRequestException("Genre name already exists")))
                .switchIfEmpty(Mono.defer(() -> {  // ✅ Đặt ở đúng vị trí
                    GenreEntity genreEntity = new GenreEntity();
                    modelMapper.map(genreRequest, genreEntity);
                    genreEntity.setCreatedAt(LocalDateTime.now());
                    return genreRepository.save(genreEntity)
                            .map(response -> modelMapper.map(response, GenreResponse.class));
                })).cast(GenreResponse.class);
    }

    @Override
    public Mono<GenreResponse> getGenre(String genreId) {
        return genreRepository.findById(Integer.parseInt(genreId))
                .switchIfEmpty(Mono.error(new BadRequestException("Genre id not found")))
                .map(genreEntity -> modelMapper.map(genreEntity, GenreResponse.class));
    }

    @Override
    public Mono<GenreResponse> deleteGenre(String genreId) {
        return genreRepository.findById(Integer.parseInt(genreId))
                .switchIfEmpty(Mono.error(new BadRequestException("Genre id not found")))
                .flatMap(genreEntity -> genreRepository.delete(genreEntity)
                        .then(Mono.just(modelMapper.map(genreEntity, GenreResponse.class))));
    }

    @Override
    public Flux<GenreResponse> getAllGenres() {
        return genreRepository.findAll()
                .map(genreEntity -> modelMapper.map(genreEntity, GenreResponse.class));
    }
}
