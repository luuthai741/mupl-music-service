package com.mupl.music_service.utils.constain;

public enum DateTimePattern {
    DD_MM_YYYY("dd/MM/yyyy"),
    DD_MM_YYYY_HH_MM_SS("dd/MM/yyyy HH:mm:ss");

    public final String pattern;

    DateTimePattern(String pattern) {
        this.pattern = pattern;
    }

}
