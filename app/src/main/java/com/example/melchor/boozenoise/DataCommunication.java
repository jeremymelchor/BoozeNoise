package com.example.melchor.boozenoise;

public interface DataCommunication {

    void setLatitude(double latitude);

    void setLongitude(double longitude);

    double getLatitude();

    double getLongitude();

    int getRadiusInMeters();
}
