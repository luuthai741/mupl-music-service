package com.mupl.music_service.handler;

import com.mupl.music_service.dto.request.album.AlbumCreateRequest;
import com.mupl.music_service.dto.response.ErrorResponse;
import com.mupl.music_service.dto.response.PageableResponse;
import com.mupl.music_service.exception.BadRequestException;
import com.mupl.music_service.service.AlbumService;
import com.mupl.music_service.utils.RequestUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class AlbumHandler {
    private final AlbumService albumService;

    public Mono<ServerResponse> createAlbum(ServerRequest request) {
        return request.bodyToMono(AlbumCreateRequest.class)
                .flatMap(createRequest -> albumService.createAlbum(createRequest)
                        .flatMap(response -> ServerResponse.status(HttpStatus.CREATED).bodyValue(response)))
                .onErrorResume(BadRequestException.class, e -> ServerResponse
                        .badRequest()
                        .bodyValue(new ErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage())));
    }

    public Mono<ServerResponse> deleteAlbum(ServerRequest request) {
        String id = request.pathVariable("id");
        return albumService.deleteAlbum(id)
                .flatMap(response -> ServerResponse.ok().bodyValue(response))
                .onErrorResume(BadRequestException.class, e -> ServerResponse
                        .badRequest()
                        .bodyValue(new ErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage())));
    }

    public Mono<ServerResponse> getAllAlbums(ServerRequest request) {
        Pageable pageable = RequestUtils.getPageable(request,"albumId");
        String artistId = RequestUtils.getPathVariable(request,"artistId");
        return ServerResponse.ok().body(albumService.getAlbums(pageable, artistId), PageableResponse.class);
    }

    public Mono<ServerResponse> getAlbumById(ServerRequest request) {
        String id = request.pathVariable("id");
        return albumService.getAlbum(id)
                .flatMap(response -> ServerResponse.ok().bodyValue(response))
                .onErrorResume(BadRequestException.class, e -> ServerResponse
                        .badRequest()
                        .bodyValue(new ErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage())));
    }

    public Mono<ServerResponse> updateAlbum(ServerRequest request) {
        String id = request.pathVariable("id");
        return request.bodyToMono(AlbumCreateRequest.class)
                .flatMap(createRequest -> albumService.updateAlbum(id, createRequest)
                        .flatMap(response -> ServerResponse.ok().bodyValue(response)))
                .onErrorResume(BadRequestException.class, e -> ServerResponse
                        .badRequest()
                        .bodyValue(new ErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage())));
    }
}
