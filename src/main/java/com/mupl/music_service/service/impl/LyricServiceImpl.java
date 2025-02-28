package com.mupl.music_service.service.impl;

import com.mupl.music_service.dto.request.LyricRequest;
import com.mupl.music_service.dto.response.LyricDetailResponse;
import com.mupl.music_service.dto.response.LyricResponse;
import com.mupl.music_service.dto.response.PageableResponse;
import com.mupl.music_service.entity.LyricDetailEntity;
import com.mupl.music_service.entity.LyricEntity;
import com.mupl.music_service.exception.BadRequestException;
import com.mupl.music_service.repository.LyricDetailRepository;
import com.mupl.music_service.repository.LyricRepository;
import com.mupl.music_service.service.LyricService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class LyricServiceImpl implements LyricService {
    private final LyricRepository lyricRepository;
    private final LyricDetailRepository lyricDetailRepository;
    private final ModelMapper modelMapper;

    @Override
    public Mono<LyricResponse> createLyric(LyricRequest lyricRequest) {
        return lyricRepository.existsBySongId(lyricRequest.getSongId())
                .flatMap(exists -> {
                    if (exists) {
                        return Mono.error(new BadRequestException("Song has already been bound a lyric"));
                    }
                    LyricEntity lyricEntity = new LyricEntity();
                    modelMapper.map(lyricRequest, lyricEntity);
                    return lyricRepository.save(lyricEntity)
                            .flatMap(lyric -> {
                                List<LyricDetailEntity> details = lyricRequest.getDetails()
                                        .stream()
                                        .map(d -> {
                                            LyricDetailEntity lyricDetailEntity = new LyricDetailEntity();
                                            modelMapper.map(d, lyricDetailEntity);
                                            lyricDetailEntity.setLyricId(lyric.getLyricId());
                                            return lyricDetailEntity;
                                        })
                                        .toList();
                                return lyricDetailRepository.saveAll(details)
                                        .collectList()
                                        .map(savedDetails -> {
                                            List<LyricDetailResponse> detailsResponse = savedDetails
                                                    .stream()
                                                    .map(d -> modelMapper.map(d, LyricDetailResponse.class))
                                                    .toList();
                                            LyricResponse lyricResponse = new LyricResponse();
                                            modelMapper.map(lyric, lyricResponse);
                                            lyricResponse.setDetails(detailsResponse);
                                            return lyricResponse;
                                        });
                            });
                });
    }

    @Override
    public Mono<LyricResponse> updateLyric(String lyricId, LyricRequest lyricRequest) {
        return lyricRepository.findById(Long.parseLong(lyricId))
                .switchIfEmpty(Mono.error(new BadRequestException("Lyric not found")))
                .flatMap(lyricEntity -> {
                    lyricEntity.setSongId(lyricRequest.getSongId());
                    lyricEntity.setName(lyricRequest.getName());
                    return lyricRepository.save(lyricEntity)
                            .flatMap(savedLyricEntity -> deleteLyricDetailByLyricId(savedLyricEntity.getLyricId())
                                    .then(Mono.defer(() -> {
                                        List<LyricDetailEntity> details = lyricRequest.getDetails()
                                                .stream()
                                                .map(d -> {
                                                    LyricDetailEntity lyricDetailEntity = new LyricDetailEntity();
                                                    modelMapper.map(d, lyricDetailEntity);
                                                    lyricDetailEntity.setLyricId(savedLyricEntity.getLyricId());
                                                    return lyricDetailEntity;
                                                })
                                                .toList();
                                        return lyricDetailRepository.saveAll(details)
                                                .collectList()
                                                .map(savedDetails -> {
                                                    List<LyricDetailResponse> detailsResponse = savedDetails
                                                            .stream()
                                                            .map(d -> modelMapper.map(d, LyricDetailResponse.class))
                                                            .toList();
                                                    LyricResponse lyricResponse = new LyricResponse();
                                                    modelMapper.map(savedLyricEntity, lyricResponse);
                                                    lyricResponse.setDetails(detailsResponse);
                                                    return lyricResponse;
                                                });
                                    })));
                });
    }

    @Override
    public Mono<PageableResponse> getLyrics(Pageable pageable) {
        return lyricRepository.findAllBy(pageable)
                .collectList()
                .zipWith(lyricRepository.count())
                .map(tuple -> new PageableResponse(tuple.getT1(), pageable, tuple.getT2()));
    }

    public Mono<Void> deleteLyricDetailByLyricId(Long lyricId) {
        return lyricDetailRepository.deleteByLyricId(lyricId)
                .then();
    }

    public Mono<LyricResponse> getLyric(String lyricId) {
        return lyricRepository.findById(Long.parseLong(lyricId))
                .switchIfEmpty(Mono.error(new BadRequestException("Lyric not found")))
                .flatMap(lyricEntity -> lyricDetailRepository.findAllByLyricId(lyricEntity.getLyricId())
                        .collectList()
                        .map(details -> {
                            List<LyricDetailResponse> detailsResponse = details
                                    .stream()
                                    .map(d -> modelMapper.map(d, LyricDetailResponse.class))
                                    .toList();
                            LyricResponse lyricResponse = new LyricResponse();
                            modelMapper.map(lyricEntity, lyricResponse);
                            lyricResponse.setDetails(detailsResponse);
                            return lyricResponse;
                        }));
    }

    @Override
    public Mono<LyricResponse> getLyricBySongId(String songId) {
        return lyricRepository.existsBySongId(Long.parseLong(songId))
                .flatMap(exists -> {
                    if (!exists) {
                        return Mono.error(new BadRequestException("Song has already been bound a lyric"));
                    }
                    return lyricRepository.findBySongId(Long.parseLong(songId))
                            .flatMap(lyricEntity -> getLyric(String.valueOf(lyricEntity.getLyricId())));
                });
    }

    @Override
    public Mono<LyricResponse> deleteLyric(String lyricId) {
        return lyricRepository.findById(Long.parseLong(lyricId))
                .switchIfEmpty(Mono.error(new BadRequestException("Lyric not found!")))
                .flatMap(entity -> lyricRepository.delete(entity).then(Mono.just(modelMapper.map(entity, LyricResponse.class))
                        .doOnSuccess(e -> deleteLyricDetailByLyricId(e.getLyricId())
                                .subscribeOn(Schedulers.boundedElastic())
                                .subscribe())));
    }
}
