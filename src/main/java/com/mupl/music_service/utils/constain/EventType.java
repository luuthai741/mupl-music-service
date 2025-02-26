package com.mupl.music_service.utils.constain;

public enum EventType {
    SONG_UPLOADED,
    IMAGE_UPLOADED;

    public static String getContentTypeByEventType(EventType eventType) {
        return switch (eventType) {
            case SONG_UPLOADED -> "audio/mpeg";
            case IMAGE_UPLOADED -> "image/jpeg";
        };
    }
}
