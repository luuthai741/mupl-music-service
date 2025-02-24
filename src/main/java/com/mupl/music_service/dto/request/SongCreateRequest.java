package com.mupl.music_service.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SongCreateRequest {
    private String title;
    private Set<String> artists;
    private String album;
    private String genre;
    private FilePart file;
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate releasedAt;
}
