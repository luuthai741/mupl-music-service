package com.mupl.music_service.kafka.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FilePayload {
    private String filename;
    private String contentType;
    private Long songId;
    private byte[] data;
}
