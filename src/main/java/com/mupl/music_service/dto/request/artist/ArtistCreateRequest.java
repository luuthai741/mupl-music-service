package com.mupl.music_service.dto.request.artist;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mupl.music_service.utils.constain.Country;
import com.mupl.music_service.utils.constain.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArtistCreateRequest {
    private String name;
    private Gender gender;
    private Country country;
    private String description;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate birthday;
}
