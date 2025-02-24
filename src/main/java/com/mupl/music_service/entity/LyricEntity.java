package com.mupl.music_service.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "mupl_lyric")
public class LyricEntity {
    @Id
    private Long lyricId;
    private Long songQuantityId;
    private Integer markedAt;
}
