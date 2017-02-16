package com.example.melchor.boozenoise.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Bar implements Parcelable {

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

    public void setPlace_id(String place_id) {
        this.place_id = place_id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    public void setDecibels(double decibels) {
        this.decibels = decibels;
    }

    public void setListPhotos(ArrayList<Photo> listPhotos) {
        this.listPhotos = listPhotos;
    }

    @Override
    public String toString() {
        return "Bar{" +
                "place_id='" + place_id + '\'' +
                ", name='" + name + '\'' +
                ", rating=" + rating +
                ", vicinity='" + vicinity + '\'' +
                ", geometry=" + geometry +
                ", decibels=" + decibels +
                ", listPhotos=" + listPhotos +
                '}';
    }

    //==============================================================================================
    // Parcelable implementation
    //==============================================================================================

    protected Bar(Parcel in) {
        place_id = in.readString();
        name = in.readString();
        rating = in.readFloat();
        vicinity = in.readString();
        geometry = in.readParcelable(Geometry.class.getClassLoader());
        decibels = in.readDouble();
        listPhotos = in.createTypedArrayList(Photo.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(place_id);
        dest.writeString(name);
        dest.writeFloat(rating);
        dest.writeString(vicinity);
        dest.writeParcelable(geometry, flags);
        dest.writeDouble(decibels);
        dest.writeTypedList(listPhotos);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Bar> CREATOR = new Creator<Bar>() {
        @Override
        public Bar createFromParcel(Parcel in) {
            return new Bar(in);
        }

        @Override
        public Bar[] newArray(int size) {
            return new Bar[size];
        }
    };

}
