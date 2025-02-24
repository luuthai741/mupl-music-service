package com.mupl.music_service.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "mupl_album")
public class AlbumEntity {
    @Id
    private Integer albumId;
    private String name;
    private Integer artistId;
    private LocalDateTime createdAt;
    private LocalDateTime releasedAt;
}
