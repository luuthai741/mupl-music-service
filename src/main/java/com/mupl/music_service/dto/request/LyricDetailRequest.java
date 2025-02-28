package com.mupl.music_service.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LyricDetailRequest {
    private int startTime;
    private int endTime;
    private String lyric;
}
