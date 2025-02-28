package com.mupl.music_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

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

    public PageableResponse(List<?> content, Pageable pageable, long totalElements) {
        Page<?> pageImpl = new PageImpl<>(content, pageable, totalElements);
        this.page = pageImpl.getNumber() + 1;
        this.pageSize = pageImpl.getSize();
        this.totalPages = pageImpl.getTotalPages();
        this.totalElements = pageImpl.getTotalElements();
        this.content = content;
    }
}
