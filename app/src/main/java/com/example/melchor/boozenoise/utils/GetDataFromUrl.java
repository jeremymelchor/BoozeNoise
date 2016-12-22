package com.example.melchor.boozenoise.utils;

import android.os.AsyncTask;
import android.util.Log;

import com.example.melchor.boozenoise.entities.Bar;
import com.example.melchor.boozenoise.entities.ListBars;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GetDataFromUrl extends AsyncTask<Void,Void,ListBars> {

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
    protected ListBars doInBackground(Void... voids) {
        HttpHandler httpHandler = new HttpHandler();

        String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?"
                +"location="+latitude+','+longitude
                +"&radius="+radius_in_meters
                +"&type=bar"
                +"&key="+KEY;

        Log.d(TAG,"URL : "+url);

        // Making a request to url and getting response
        String jsonStr = httpHandler.makeServiceCall(url);

        Log.e(TAG, "Response from url: " + jsonStr);

        Gson gson = new GsonBuilder().create();
        ListBars listBars = gson.fromJson(jsonStr, ListBars.class);

        return listBars;
    }

    protected void onPostExecute(ListBars listBars) {
        Log.d(TAG,"WRITING TO DB..");
        // Persist bars found
        DatabaseManager databaseManager = new DatabaseManager("write",listBars);
        databaseManager.execute();

        // Update map
        for (Bar bar : listBars.getResultsFromWebservice()) {
            double latitude = bar.getGeometry().getLocation().getLatitude();
            double longitude = bar.getGeometry().getLocation().getLongitude();
            LatLng latLng = new LatLng(latitude,longitude);

            googleMap.addMarker(new MarkerOptions().position(latLng).title(bar.getName()));
        }
    }
}
