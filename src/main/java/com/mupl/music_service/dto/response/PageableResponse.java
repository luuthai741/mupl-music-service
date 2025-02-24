package com.mupl.music_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PageableResponse {
    private int page;
    private int pageSize;
    private int totalPages;
    private long totalElements;
    private List<?> content;
}
