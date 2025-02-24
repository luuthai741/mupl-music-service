package com.mupl.music_service.utils;

import com.mupl.music_service.utils.constain.DateTimePattern;
import org.springframework.http.codec.multipart.FilePart;

import java.io.File;
import java.io.IOException;

public class FileUtils {
    public static String getFileName(String songName) {
        return songName.replace(" ", "_") + ".mp3";
    }

    public static File convertMultipartFileToFile(FilePart originalFile) throws IOException {
        File file = File.createTempFile("_updload",".mp3");
        originalFile.transferTo(file);
        return file;
    }
}
