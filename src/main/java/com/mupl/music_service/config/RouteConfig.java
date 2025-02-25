package com.mupl.music_service.config;

import com.mupl.music_service.handler.AlbumHandler;
import com.mupl.music_service.handler.ArtistHandler;
import com.mupl.music_service.handler.GenreHandler;
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
                .andRoute(RequestPredicates.GET(convertUri("artists")), artistHandler::getArtists)
                .andRoute(RequestPredicates.GET(convertUri("artists/{id}")), artistHandler::getArtist)
                .andRoute(RequestPredicates.PUT(convertUri("artists/{id}")), artistHandler::updateArtist)
                .andRoute(RequestPredicates.DELETE(convertUri("artists/{id}")),artistHandler::deleteArtist);
    }

    @Bean(name = "genreRouter")
    public RouterFunction<ServerResponse> genreRouterFunction(GenreHandler genreHandler) {
        return RouterFunctions
                .route(RequestPredicates.POST(convertUri("genres")), genreHandler::createGenre)
                .andRoute(RequestPredicates.GET(convertUri("genres")), genreHandler::getAllGenres)
                .andRoute(RequestPredicates.GET(convertUri("genres/{id}")), genreHandler::getGenre)
                .andRoute(RequestPredicates.DELETE(convertUri("genres/{id}")),genreHandler::deleteGenre);
    }

    @Bean(name = "albumRouter")
    public RouterFunction<ServerResponse> albumRouterFunction(AlbumHandler albumHandler) {
        return RouterFunctions
                .route(RequestPredicates.POST(convertUri("albums")), albumHandler::createAlbum)
                .andRoute(RequestPredicates.GET(convertUri("albums")), albumHandler::getAllAlbums)
                .andRoute(RequestPredicates.GET(convertUri("artists/{artistId}/albums")), albumHandler::getAllAlbums)
                .andRoute(RequestPredicates.GET(convertUri("albums/{id}")), albumHandler::getAlbumById)
                .andRoute(RequestPredicates.PUT(convertUri("albums/{id}")), albumHandler::updateAlbum)
                .andRoute(RequestPredicates.DELETE(convertUri("albums/{id}")),albumHandler::deleteAlbum);
    }
}
