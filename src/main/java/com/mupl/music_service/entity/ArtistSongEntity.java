package com.mupl.music_service.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "mupl_artists_songs")
public class ArtistSongEntity {
    @Id
    private Long id;
    private Long songId;
    private Integer artistId;
}
