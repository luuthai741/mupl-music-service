package com.mupl.music_service.service;

import com.mupl.music_service.dto.request.album.AlbumCreateRequest;
import com.mupl.music_service.dto.response.AlbumResponse;
import com.mupl.music_service.dto.response.PageableResponse;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Mono;

public interface AlbumService {
    Mono<AlbumResponse> createAlbum(AlbumCreateRequest request);

    Mono<AlbumResponse> getAlbum(String id);

    Mono<AlbumResponse> updateAlbum(String id, AlbumCreateRequest request);

    Mono<AlbumResponse> deleteAlbum(String id);

    Mono<PageableResponse> getAlbums(Pageable pageable, String artistId);
}
