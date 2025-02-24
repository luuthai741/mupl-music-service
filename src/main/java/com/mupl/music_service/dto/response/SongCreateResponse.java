package com.mupl.music_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SongCreateResponse {
    private Long songId;
    private Set<String> artists;
    private String album;
    private String genre;
    private Integer duration;
    private String title;
    private String imagePath;
}
