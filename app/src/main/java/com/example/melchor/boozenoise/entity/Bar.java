package com.example.melchor.boozenoise.entity;

import java.util.ArrayList;

public class Bar {

    private String name;
    private float rating;
    private String vicinity;
    private Geometry geometry;
    private ArrayList photos;

    public Bar() {
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "name : " + name + "\n"
                + "ratings : " + rating + "\n"
                + "vicinity : " + vicinity + "\n"
                + geometry.toString() + "\n"
                + photos.toString();
    }
}
