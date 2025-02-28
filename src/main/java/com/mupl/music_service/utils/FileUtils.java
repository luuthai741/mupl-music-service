package com.mupl.music_service.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Slf4j
public class FileUtils {
    public static String getFileName(Long songId, String songName) {
        String extension = FilenameUtils.getExtension(songName);
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd_MM_yyyy");
        String date = LocalDateTime.now().format(dateFormat);
        return String.format("%s/%s_%s.%s", songId, date, UUID.randomUUID(), extension);
    }
}
