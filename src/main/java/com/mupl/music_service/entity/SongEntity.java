package com.mupl.music_service.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "mupl_song")
public class SongEntity {
    @Id
    private Long songId;
    private String title;
    private Integer albumId;
    private String imagePath;
    private String songPath;
    private LocalDate releasedAt;
    private Boolean isFreeToPlay;
    private Integer duration;
    private LocalDateTime updatedAt;
    private LocalDateTime createdAt;
}
