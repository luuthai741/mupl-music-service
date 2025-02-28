package com.mupl.music_service.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LyricRequest {
    private String name;
    private Long songId;
    List<LyricDetailRequest> details;
}
