package com.mupl.music_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LyricDetailResponse {
    private String lyric;
    private int startTime;
    private int endTime;
}
