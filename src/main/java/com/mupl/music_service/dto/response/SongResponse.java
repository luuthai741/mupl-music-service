package com.mupl.music_service.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SongResponse {
    private long songId;
    private String title;
    private String album;
    @JsonIgnore
    private String imagePath;
    @JsonIgnore
    private String songPath;
    private Boolean isFreeToPlay;
    private int duration;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyy")
    private LocalDate releasedAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyy HH:mm:ss")
    private LocalDateTime updatedAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyy HH:mm:ss")
    private LocalDateTime createdAt;
}
