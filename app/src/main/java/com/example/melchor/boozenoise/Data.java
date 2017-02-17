package com.example.melchor.boozenoise;

import android.content.res.Resources;
import android.util.DisplayMetrics;

import com.example.melchor.boozenoise.entities.Bar;

import java.util.ArrayList;

public class Data {

    private static final int SAMPLE_DELAY = 2000;
    private static final int SAMPLE_RATE = 8000;
    private static final int AUDIO_SAMPLES = 5;
    private static final String KEY = "AIzaSyBKlwR3R7oovTzIh7xepPRrbIO6n-da6jQ";

    private static double latitude, longitude;
    private static int radiusInMeters = 1000;
    private static boolean openNow = false;
    private static ArrayList<Bar> currentListBars;

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

    public static boolean isOpenNow() {
        return openNow;
    }

    public static void setOpenNow(boolean openNow) {
        Data.openNow = openNow;
    }

    public static ArrayList<Bar> getCurrentListBars() {
        return currentListBars;
    }

    public static void setCurrentListBars(ArrayList<Bar> currentListBars) {
        Data.currentListBars = currentListBars;
    }

    //==============================================================================================
    // Utils
    //==============================================================================================

    public static String getBarsAroundMeUrl() {
        return "https://boozenoise-admin.herokuapp.com/api/nearbysearch";
    }

    public static float dpToPx(int dp) {
        DisplayMetrics displayMetrics = Resources.getSystem().getDisplayMetrics();
        return dp * ((float) displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }
}
