package com.example.melchor.boozenoise.utils;

import android.os.AsyncTask;
import android.util.Log;

import com.example.melchor.boozenoise.entities.Bar;
import com.example.melchor.boozenoise.entities.ListBars;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

public class GetCurrentBar extends AsyncTask<Void,Void,ListBars> {

    private static final String TAG = GetCurrentBar.class.getSimpleName();
    private final String KEY = "AIzaSyBKlwR3R7oovTzIh7xepPRrbIO6n-da6jQ";
    private double latitude,longitude;
    private final int RADIUS_IN_METERS = 50;
    private OnBarsFetched listener;

    public GetCurrentBar(double latitude, double longitude, OnBarsFetched listener) {
        super();
        this.latitude = latitude;
        this.longitude = longitude;
        this.listener = listener;
    }

    @Override
    protected ListBars doInBackground(Void... voids) {
        HttpHandler httpHandler = new HttpHandler();

        String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?"
                +"location="+latitude+','+longitude
                +"&radius="+ RADIUS_IN_METERS
                +"&type=bar"
                +"&key="+KEY;

        // Making a request to url and getting response
        String jsonStr = httpHandler.makeServiceCall(url);

        Gson gson = new GsonBuilder().create();
        ListBars listBars = gson.fromJson(jsonStr, ListBars.class);

        return listBars;
    }

    protected void onPostExecute(ListBars listBars) {
        ArrayList<String> list = new ArrayList<>();

        for (Bar bar : listBars.getResultsFromWebservice()) {
            list.add(bar.getName());
        }

        if (listener != null) listener.onBarsFetched(list);
    }

    public interface OnBarsFetched {
        void onBarsFetched(ArrayList<String> listBars);
    }
}
