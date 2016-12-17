package com.example.melchor.boozenoise.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Bar {

    private String place_id;
    private String name;
    private float rating;
    private String vicinity;
    private Geometry geometry;

    @SerializedName("listPhotos")
    private ArrayList<Photo> listPhotos;

    public Bar() {}

    public String getName() {
        return name;
    }

    public String getPlace_id() {
        return place_id;
    }

    public float getRating() {
        return rating;
    }

    public String getVicinity() {
        return vicinity;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public ArrayList<Photo> getPhotos() {
        return listPhotos;
    }

    @Override
    public String toString() {
        return "place_id : " + place_id + "\n"
                + "name : " + name + "\n"
                + "ratings : " + rating + "\n"
                + "vicinity : " + vicinity + "\n"
                + geometry.toString() + "\n"
                + listPhotos.toString();
    }
}
