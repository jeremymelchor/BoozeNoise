package com.example.melchor.boozenoise.utils;

import android.os.AsyncTask;
import android.util.Log;

import com.example.melchor.boozenoise.entity.ListBar;
import com.google.android.gms.maps.GoogleMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GetDataFromUrl extends AsyncTask<Void,Void,Void> {

    private static final String TAG = GetDataFromUrl.class.getSimpleName();
    private final String KEY = "AIzaSyBKlwR3R7oovTzIh7xepPRrbIO6n-da6jQ";
    private double latitude,longitude;
    private double radius_in_meters;
    private GoogleMap googleMap;

    public GetDataFromUrl(double latitude, double longitude, int radius_in_meters, GoogleMap googleMap) {
        super();
        this.latitude = latitude;
        this.longitude = longitude;
        this.radius_in_meters = radius_in_meters;
        this.googleMap = googleMap;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        HttpHandler httpHandler = new HttpHandler();

        String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?"
                +"location="+latitude+','+longitude
                +"&radius="+radius_in_meters
                +"&type=bar"
                +"&opennow"
                +"&key="+KEY;

        Log.d(TAG,"URL : "+url);

        // Making a request to url and getting response
        String jsonStr = httpHandler.makeServiceCall(url);

        Log.e(TAG, "Response from url: " + jsonStr);

        Gson gson = new GsonBuilder().create();
        ListBar listBar = gson.fromJson(jsonStr, ListBar.class);
        Log.d(TAG, listBar.toString());

        return null;
    }

    protected void onPostExecute(Void voids) {

    }
}
