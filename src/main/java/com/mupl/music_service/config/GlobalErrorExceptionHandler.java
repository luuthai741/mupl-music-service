package com.mupl.music_service.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mupl.music_service.dto.response.ErrorResponse;
import com.mupl.music_service.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;


@Component
@Slf4j
@RequiredArgsConstructor
public class GlobalErrorExceptionHandler implements WebExceptionHandler {
    private final ObjectMapper objectMapper;

    @NotNull
    @Override
    public Mono<Void> handle(@NotNull ServerWebExchange exchange, Throwable ex) {
        log.error("Handling exception", ex);

        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        String message = "Unexpected error";

        if (ex instanceof BadRequestException) {
            status = HttpStatus.BAD_REQUEST;
            message = ex.getMessage();
        }

        ErrorResponse errorResponse = new ErrorResponse(status.value(), message);
        exchange.getResponse().setStatusCode(status);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

        DataBuffer buffer;
        try {
            byte[] bytes = objectMapper.writeValueAsBytes(errorResponse);
            buffer = exchange.getResponse().bufferFactory().wrap(bytes);
        } catch (Exception e) {
            buffer = exchange.getResponse().bufferFactory().wrap("Error processing response".getBytes());
        }

        return exchange.getResponse().writeWith(Mono.just(buffer));
    }
}
