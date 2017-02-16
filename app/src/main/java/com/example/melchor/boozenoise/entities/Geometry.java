package com.example.melchor.boozenoise.entities;

import android.os.Parcel;
import android.os.Parcelable;

public class Geometry implements Parcelable {

    private Location location;

    public Geometry(Location location) {
        this.location = location;
    }

    public Location getLocation() { return location; }

    @Override
    public String toString() {
        return location.toString();
    }

    //==============================================================================================
    // Parcelable implementation
    //==============================================================================================

    protected Geometry(Parcel in) {
        location = in.readParcelable(Location.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(location, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Geometry> CREATOR = new Creator<Geometry>() {
        @Override
        public Geometry createFromParcel(Parcel in) {
            return new Geometry(in);
        }

        @Override
        public Geometry[] newArray(int size) {
            return new Geometry[size];
        }
    };
}
