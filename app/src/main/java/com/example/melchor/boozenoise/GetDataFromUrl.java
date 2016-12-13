package com.example.melchor.boozenoise;

import android.os.AsyncTask;
import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;

import static android.provider.ContactsContract.CommonDataKinds.Website.URL;

public class GetDataFromUrl extends AsyncTask<Void,Void,Void> {

    private static final String TAG = GetDataFromUrl.class.getSimpleName();
    private final String KEY = "AIzaSyBKlwR3R7oovTzIh7xepPRrbIO6n-da6jQ";
    private double latitude,longitude;
    private double radius_in_meters;

    public GetDataFromUrl(double latitude, double longitude, int radius_in_meters) {
        super();
        this.latitude = latitude;
        this.longitude = longitude;
        this.radius_in_meters = radius_in_meters;
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
        return null;

    }
}
