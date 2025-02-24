package com.mupl.music_service.handler;

import com.mupl.music_service.dto.request.genre.GenreRequest;
import com.mupl.music_service.dto.response.ErrorResponse;
import com.mupl.music_service.exception.BadRequestException;
import com.mupl.music_service.service.GenreService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
public class GenreHandler {
    private final GenreService genreService;

    public Mono<ServerResponse> createGenre(ServerRequest request) {
        return request.bodyToMono(GenreRequest.class)
                .flatMap(genreRequest -> genreService.createGenre(genreRequest)
                        .flatMap(genreResponse -> ServerResponse.ok().bodyValue(genreResponse)))
                .onErrorResume(BadRequestException.class, e ->
                        ServerResponse.status(HttpStatus.BAD_REQUEST)
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage()))
                );
    }

    public Mono<ServerResponse> getGenre(ServerRequest request) {
        String genreId = request.pathVariable("id");
        return genreService.getGenre(genreId)
                .flatMap(genreResponse -> ServerResponse.ok().bodyValue(genreResponse))
                .onErrorResume(BadRequestException.class, e ->
                        ServerResponse.status(HttpStatus.BAD_REQUEST)
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage()))
                );
    }

    public Mono<ServerResponse> deleteGenre(ServerRequest request) {
        String genreId = request.pathVariable("id");
        return genreService.deleteGenre(genreId)
                .flatMap(genreResponse -> ServerResponse.ok().bodyValue(genreResponse))
                .onErrorResume(BadRequestException.class, e ->
                        ServerResponse.status(HttpStatus.BAD_REQUEST)
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage()))
                );
    }

    public Mono<ServerResponse> getAllGenres(ServerRequest request) {
        return genreService.getAllGenres()
                .collectList()
                .flatMap(genre -> ServerResponse.ok().bodyValue(genre));
    }

}
