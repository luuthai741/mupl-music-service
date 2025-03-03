package com.mupl.music_service.handler.internal;

import com.mupl.music_service.dto.response.ErrorResponse;
import com.mupl.music_service.exception.BadRequestException;
import com.mupl.music_service.service.SongService;
import com.mupl.music_service.utils.RequestUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class SongInternalHandler {
    private final SongService songService;

    public Mono<ServerResponse> getSong(ServerRequest request) {
        String id = RequestUtils.getPathVariable(request, "id");
        return songService.getSongInfo(id)
                .flatMap(response -> ServerResponse.ok().bodyValue(response))
                .onErrorResume(BadRequestException.class, e -> ServerResponse.badRequest().bodyValue(new ErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage())));
    }
}
