package com.mupl.music_service.utils.constain;

public enum Gender {
    MALE("Male"),
    FEMALE("Female"),
    OTHER("Other");
    private String value;

    Gender(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
