package com.mupl.music_service.config;

import com.mupl.music_service.handler.ArtistHandler;
import com.mupl.music_service.handler.SongHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class RouteConfig {
    private String convertUri(String uri) {
        final String PREFIX = "/api/v1/mupl/";
        return PREFIX + uri;
    }

    @Bean(name = "songRouter")
    public RouterFunction<ServerResponse> songRouterFunction(SongHandler songHandler) {
        return RouterFunctions
                .route(RequestPredicates.POST(convertUri("songs")), songHandler::createSong)
                .andRoute(RequestPredicates.GET(convertUri("songs/{name}")), songHandler::createSong);
    }

    @Bean(name = "artistRouter")
    public RouterFunction<ServerResponse> artistRouterFunction(ArtistHandler artistHandler) {
        return RouterFunctions
                .route(RequestPredicates.POST(convertUri("artists")), artistHandler::createArtist)
                .andRoute(RequestPredicates.GET(convertUri("artists")), artistHandler::getArtists);
    }
}
