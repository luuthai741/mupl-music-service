package com.mupl.music_service.handler;

import com.mupl.music_service.dto.request.LyricRequest;
import com.mupl.music_service.dto.response.ErrorResponse;
import com.mupl.music_service.dto.response.PageableResponse;
import com.mupl.music_service.exception.BadRequestException;
import com.mupl.music_service.service.LyricService;
import com.mupl.music_service.utils.RequestUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;


@Component
@RequiredArgsConstructor
public class LyricHandler {
    private final LyricService lyricService;

    public Mono<ServerResponse> createLyric(ServerRequest request) {
        return request.bodyToMono(LyricRequest.class)
                .flatMap(lyricRequest -> lyricService.createLyric(lyricRequest)
                        .flatMap(response -> ServerResponse
                                .ok()
                                .bodyValue(response))
                        .onErrorResume(BadRequestException.class, e -> ServerResponse
                                .badRequest()
                                .bodyValue(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage()))));
    }

    public Mono<ServerResponse> updateLyric(ServerRequest request) {
        String id = RequestUtils.getPathVariable(request, "id");
        return request.bodyToMono(LyricRequest.class)
                .flatMap(lyricRequest -> lyricService.updateLyric(id, lyricRequest)
                        .flatMap(response -> ServerResponse
                                .ok()
                                .bodyValue(response))
                        .onErrorResume(BadRequestException.class, e -> ServerResponse
                                .badRequest()
                                .bodyValue(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage()))));
    }

    public Mono<ServerResponse> getLyrics(ServerRequest request) {
        Pageable pageable = RequestUtils.getPageable(request,"lyricId");
        return ServerResponse.ok().body(lyricService.getLyrics(pageable), PageableResponse.class);
    }

    public Mono<ServerResponse> getLyric(ServerRequest request) {
        String id = RequestUtils.getPathVariable(request, "id");
        return lyricService.getLyric(id)
                .flatMap(response -> ServerResponse
                        .ok()
                        .bodyValue(response))
                .onErrorResume(BadRequestException.class, e -> ServerResponse
                        .badRequest()
                        .bodyValue(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage())));
    }
    public Mono<ServerResponse> getLyricBySong(ServerRequest request) {
        String id = RequestUtils.getPathVariable(request, "id");
        return lyricService.getLyricBySongId(id)
                .flatMap(response -> ServerResponse
                        .ok()
                        .bodyValue(response))
                .onErrorResume(BadRequestException.class, e -> ServerResponse
                        .badRequest()
                        .bodyValue(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage())));
    }
    public Mono<ServerResponse> deleteAlbum(ServerRequest request) {
        String id = request.pathVariable("id");
        return lyricService.deleteLyric(id)
                .flatMap(response -> ServerResponse.ok().bodyValue(response))
                .onErrorResume(BadRequestException.class, e -> ServerResponse
                        .badRequest()
                        .bodyValue(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage())));
    }
}
