package com.mupl.music_service.service;

import com.mupl.music_service.dto.request.artist.ArtistCreateRequest;
import com.mupl.music_service.dto.request.artist.ArtistUpdateRequest;
import com.mupl.music_service.dto.response.ArtistResponse;
import com.mupl.music_service.dto.response.PageableResponse;
import org.springframework.data.domain.Sort;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ArtistService {
    Mono<ArtistResponse> getArtist(String id);

    Mono<ArtistResponse> createArtist(ArtistCreateRequest request);

    Mono<ArtistResponse> deleteArtist(String id);

    Mono<ArtistResponse> updateArtist(String id, ArtistUpdateRequest request);

    Mono<PageableResponse> getArtists(int page, int size, String sortBy, Sort.Direction sortOrder);
}
