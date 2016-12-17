package com.example.melchor.boozenoise.entity;

public class Photo {

    private int height;
    private int width;
    private String photo_reference;

    public int getHeight() { return height; }
    public int getWidth() { return width; }
    public String getPhoto_reference() { return photo_reference; }

    @Override
    public String toString() {
        return "[height : " + height + ", width : " + width + ", photo_reference : " + photo_reference + "]";
    }
}
