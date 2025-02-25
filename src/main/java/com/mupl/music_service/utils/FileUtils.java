package com.mupl.music_service.utils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FileUtils {
    public static String getFileName(String songName) {
        return songName.replace(" ", "_") + ".mp3";
    }
}
