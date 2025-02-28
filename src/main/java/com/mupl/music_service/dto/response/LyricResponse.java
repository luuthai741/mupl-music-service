package com.mupl.music_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LyricResponse {
    private long lyricId;
    private String name;
    private Long songId;
    private List<LyricDetailResponse> details;
}
