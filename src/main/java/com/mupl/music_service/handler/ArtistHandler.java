package com.mupl.music_service.handler;

import com.mupl.music_service.dto.request.artist.ArtistCreateRequest;
import com.mupl.music_service.dto.request.artist.ArtistUpdateRequest;
import com.mupl.music_service.dto.response.ArtistResponse;
import com.mupl.music_service.dto.response.ErrorResponse;
import com.mupl.music_service.dto.response.PageableResponse;
import com.mupl.music_service.exception.BadRequestException;
import com.mupl.music_service.service.ArtistService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
@Slf4j
public class ArtistHandler {
    private final ArtistService artistService;

    public Mono<ServerResponse> createArtist(ServerRequest request) {
        return request.bodyToMono(ArtistCreateRequest.class)
                .flatMap(artistCreateRequest -> artistService.createArtist(artistCreateRequest)
                        .flatMap(artistResponse -> ServerResponse.ok().bodyValue(artistResponse)))
                .onErrorResume(BadRequestException.class, e ->
                        ServerResponse.status(HttpStatus.BAD_REQUEST)
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage()))
                );
    }

    public Mono<ServerResponse> getArtists(ServerRequest request) {
        int page = Integer.parseInt(request.queryParam("page").orElse("1"));
        int size = Integer.parseInt(request.queryParam("size").orElse("10"));
        String sortBy = request.queryParam("sortBy").orElse("id");
        Sort.Direction sortOrder = Sort.Direction.fromString(request.queryParam("sortOrder").orElse("ASC"));
        return ServerResponse.ok().body(artistService.getArtists(page, size, sortBy, sortOrder), PageableResponse.class);
    }

    public Mono<ServerResponse> getArtist(ServerRequest request) {
        String id = request.pathVariable("id");
        return artistService.getArtist(id)
                .flatMap(artistResponse -> ServerResponse.ok().bodyValue(artistResponse))
                .onErrorResume(BadRequestException.class, e ->
                        ServerResponse.status(HttpStatus.BAD_REQUEST)
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage()))
                );
    }

    public Mono<ServerResponse> deleteArtist(ServerRequest request) {
        String id = request.pathVariable("id");
        return artistService.deleteArtist(id)
                .flatMap(artistResponse -> ServerResponse.ok().bodyValue(artistResponse))
                .onErrorResume(BadRequestException.class, e ->
                        ServerResponse.status(HttpStatus.BAD_REQUEST)
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage()))
                );
    }

    public Mono<ServerResponse> updateArtist(ServerRequest request) {
        String id = request.pathVariable("id");
        return request.bodyToMono(ArtistUpdateRequest.class)
                .flatMap(artistUpdateRequest -> artistService.updateArtist(id, artistUpdateRequest)
                        .flatMap(artistResponse -> ServerResponse.ok().bodyValue(artistResponse))
                        .onErrorResume(BadRequestException.class, e ->
                                ServerResponse.status(HttpStatus.BAD_REQUEST)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .bodyValue(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage()))
                        ));
    }
}
