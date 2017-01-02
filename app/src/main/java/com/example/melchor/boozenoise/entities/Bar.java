package com.example.melchor.boozenoise.entities;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Bar {

    private String place_id;
    private String name;
    private float rating;
    private String vicinity;
    private Geometry geometry;
    private double decibels;

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

    public double getDecibels() { return decibels; }

    @Override
    public String toString() {
        return name;
    }
}
