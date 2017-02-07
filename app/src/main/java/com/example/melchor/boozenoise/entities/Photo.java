package com.example.melchor.boozenoise.entities;

import android.os.Parcel;
import android.os.Parcelable;

public class Photo implements Parcelable {

    private int height;
    private int width;
    private String photo_reference;

    public int getHeight() { return height; }
    public int getWidth() { return width; }
    public String getPhoto_reference() { return photo_reference; }

    //==============================================================================================
    // Parcelable implementation
    //==============================================================================================


    protected Photo(Parcel in) {
        height = in.readInt();
        width = in.readInt();
        photo_reference = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(height);
        dest.writeInt(width);
        dest.writeString(photo_reference);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Photo> CREATOR = new Creator<Photo>() {
        @Override
        public Photo createFromParcel(Parcel in) {
            return new Photo(in);
        }

        @Override
        public Photo[] newArray(int size) {
            return new Photo[size];
        }
    };

}
