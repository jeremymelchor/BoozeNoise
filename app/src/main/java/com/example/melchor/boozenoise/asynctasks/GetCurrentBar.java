package com.example.melchor.boozenoise.asynctasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.melchor.boozenoise.Data;
import com.example.melchor.boozenoise.entities.Bar;
import com.example.melchor.boozenoise.entities.ListBars;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

public class GetCurrentBar extends AsyncTask<Void, Void, ListBars> {

    private static final String TAG = GetCurrentBar.class.getSimpleName();
    private final int RADIUS_IN_METERS = 50;
    private final OnBarsFetchedListener listener;

    private Activity activity;
    private ProgressDialog progressDialog;

    public GetCurrentBar(OnBarsFetchedListener listener, Activity activity) {
        super();
        this.listener = listener;
        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {
        if (activity != null) {
            progressDialog = new ProgressDialog(activity);
            progressDialog.setMessage("Loading...");
            progressDialog.setIndeterminate(false);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setCancelable(true);
            progressDialog.show();
        }
    }

    @Override
    protected ListBars doInBackground(Void... voids) {
        HttpHandler httpHandler = new HttpHandler();

        String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?"
                + "location=" + Data.getLatitude() + ',' + Data.getLongitude()
                + "&radius=" + RADIUS_IN_METERS
                + "&type=bar"
                + "&key=" + Data.getKEY();

        // Making a request to url and getting response
        String jsonStr = httpHandler.makeServiceCall(url);

        Gson gson = new GsonBuilder().create();

        return gson.fromJson(jsonStr, ListBars.class);
    }

    protected void onPostExecute(ListBars listBars) {
        /*ArrayList<Bar> list = new ArrayList<>();

        for (Bar bar : listBars.getResultsFromWebservice())
            list.add(bar);*/

        if (listener != null) listener.onBarsFetched(listBars);
        if (progressDialog != null) progressDialog.dismiss();
        /*if (activity != null && listView != null) {
            ArrayAdapter arrayAdapter = new ArrayAdapter<>(activity, android.R.layout.simple_list_item_1, list);
            listView.setAdapter(arrayAdapter);
        }*/
    }

    public interface OnBarsFetchedListener {
        void onBarsFetched(ListBars listBars);
    }

}
