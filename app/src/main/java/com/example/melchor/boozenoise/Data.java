package com.example.melchor.boozenoise;

import android.content.res.Resources;
import android.util.DisplayMetrics;

import static java.security.AccessController.getContext;

public class Data {

    private static final int SAMPLE_DELAY = 2000;
    private static final int SAMPLE_RATE = 8000;
    private static final int AUDIO_SAMPLES = 5;
    private static final String KEY = "AIzaSyBKlwR3R7oovTzIh7xepPRrbIO6n-da6jQ";

    private static double latitude, longitude;
    private static int radiusInMeters = 1000;

    public Data() {
        super();
    }

    public static double getLatitude() {
        return latitude;
    }

    public static void setLatitude(double latitude) {
        Data.latitude = latitude;
    }

    public static double getLongitude() {
        return longitude;
    }

    public static void setLongitude(double longitude) {
        Data.longitude = longitude;
    }

    public static int getRadiusInMeters() {
        return radiusInMeters;
    }

    public static void setRadiusInMeters(int radiusInMeters) {
        Data.radiusInMeters = radiusInMeters;
    }

    public static int getSampleDelay() {
        return SAMPLE_DELAY;
    }

    public static int getSampleRate() {
        return SAMPLE_RATE;
    }

    public static int getAudioSamples() {
        return AUDIO_SAMPLES;
    }

    public static String getKEY() {
        return KEY;
    }

    public static float dpToPx(int dp) {
        DisplayMetrics displayMetrics = Resources.getSystem().getDisplayMetrics();
        return dp * ((float) displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }
}
