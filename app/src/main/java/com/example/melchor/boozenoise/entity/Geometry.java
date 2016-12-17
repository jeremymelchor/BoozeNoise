package com.example.melchor.boozenoise.entity;

public class Geometry {

    private Location location;

    public Location getLocation() { return location; }

    @Override
    public String toString() {
        return location.toString();
    }
}
