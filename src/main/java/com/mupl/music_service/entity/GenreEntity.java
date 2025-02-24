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
@Table(name = "mupl_genre")
public class GenreEntity {
    @Id
    private Integer genreId;
    private String name;
    private LocalDateTime createdAt;
}
