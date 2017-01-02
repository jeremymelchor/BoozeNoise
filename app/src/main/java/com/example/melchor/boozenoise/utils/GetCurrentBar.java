package com.example.melchor.boozenoise.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.melchor.boozenoise.entities.Bar;
import com.example.melchor.boozenoise.entities.ListBars;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

public class GetCurrentBar extends AsyncTask<Void, Void, ListBars> {

    private static final String TAG = GetCurrentBar.class.getSimpleName();
    private final String KEY = "AIzaSyBKlwR3R7oovTzIh7xepPRrbIO6n-da6jQ";
    private final int RADIUS_IN_METERS = 50;

    private double latitude, longitude;
    private Activity activity;
    private ProgressDialog progressDialog;
    private ListView listView;

    public GetCurrentBar(double latitude, double longitude, Activity activity, ListView listView) {
        super();
        this.latitude = latitude;
        this.longitude = longitude;
        this.activity = activity;
        this.listView = listView;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("Loading...");
        progressDialog.setIndeterminate(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(true);
        progressDialog.show();
    }

    @Override
    protected ListBars doInBackground(Void... voids) {
        HttpHandler httpHandler = new HttpHandler();

        String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?"
                + "location=" + latitude + ',' + longitude
                + "&radius=" + RADIUS_IN_METERS
                + "&type=bar"
                + "&key=" + KEY;

        // Making a request to url and getting response
        String jsonStr = httpHandler.makeServiceCall(url);

        Gson gson = new GsonBuilder().create();
        ListBars listBars = gson.fromJson(jsonStr, ListBars.class);

        return listBars;
    }

    protected void onPostExecute(ListBars listBars) {
        ArrayList<Bar> list = new ArrayList<>();

        for (Bar bar : listBars.getResultsFromWebservice()) {
            list.add(bar);
        }

        progressDialog.dismiss();
        ArrayAdapter arrayAdapter = new ArrayAdapter<>(activity, android.R.layout.simple_list_item_1, list);
        listView.setAdapter(arrayAdapter);
    }

}
