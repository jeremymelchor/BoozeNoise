package com.example.melchor.boozenoise.entity;

public class Photo {

    private int height;
    private int width;
    private String photo_reference;

    @Override
    public String toString() {
        return "[height : " + height + ", width : " + width + ", photo_reference : " + photo_reference + "]";
    }
}
