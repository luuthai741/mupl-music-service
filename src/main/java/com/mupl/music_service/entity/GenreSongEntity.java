package com.mupl.music_service.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "mupl_genres_songs")
public class GenreSongEntity {
    @Id
    private GenreSongId genreSongId;

    public static class GenreSongId {
        private Integer genreId;
        private Long songId;
    }
}
