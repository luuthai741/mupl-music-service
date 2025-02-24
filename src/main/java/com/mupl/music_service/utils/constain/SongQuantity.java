package com.mupl.music_service.utils.constain;

public enum SongQuantity {
    _128KB("128kb"),
    _320KB("320kb");
    private String name;
    SongQuantity(String name) {
    }

    public String getName() {
        return name;
    }
}
