package com.mupl.music_service.utils.constain;

public enum FileUploadType {
    SONG_UPLOADED,
    IMAGE_UPLOADED;

    public static String getContentTypeByEventType(FileUploadType eventType) {
        return switch (eventType) {
            case SONG_UPLOADED -> "audio/mpeg";
            case IMAGE_UPLOADED -> "image/jpeg";
        };
    }
}
