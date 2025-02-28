package com.mupl.music_service.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "mupl_lyric_detail")
public class LyricDetailEntity {
    @Id
    private Long lyricDetailId;
    private int startTime;
    private int endTime;
    private String lyric;
    private Long lyricId;
}
