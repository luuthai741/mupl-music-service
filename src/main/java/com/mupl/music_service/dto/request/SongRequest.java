package com.mupl.music_service.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.codec.multipart.FilePart;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SongRequest {
    private String title;
    private List<Integer> artistIds;
    private List<Integer> genreIds;
    private Integer albumId;
    private Boolean isFreeToPlay;
    private FilePart songFile;
    private FilePart imageFile;
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate releasedAt;
}
