package com.mupl.music_service.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "mupl_artist")
public class ArtistEntity {
    @Id
    private Integer artistId;
    private String name;
    private String description;
    private String gender;
    private String country;
    private LocalDate birthday;
    private LocalDateTime createdAt;
}
