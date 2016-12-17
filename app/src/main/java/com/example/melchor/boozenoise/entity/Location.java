package com.example.melchor.boozenoise.entity;

import com.google.gson.annotations.SerializedName;

public class Location {


    @SerializedName("lat")
    private double latitude;

    @SerializedName("lng")
    private double longitude;

    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }

    @Override
    public String toString() {
        return "[latitude : "+ latitude +",longitude : "+ longitude +"]";
    }
}
