package com.mupl.music_service.entity;

import com.mupl.music_service.utils.constain.SongQuantity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "mupl_song_quantity")
public class SongQuantityEntity {
    @Id
    private Long songQuantityId;
    private SongQuantity songQuantity;
    private String songPath;
    private Long songId;
    private Integer duration;
}
