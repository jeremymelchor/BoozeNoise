package com.example.melchor.boozenoise.asynctasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
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

    private Context context;
    private ProgressDialog progressDialog;

    public GetCurrentBar(OnBarsFetchedListener listener, Context context) {
        super();
        this.listener = listener;
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading...");
        progressDialog.setIndeterminate(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(true);
        progressDialog.show();

    }

    @Override
    protected ListBars doInBackground(Void... voids) {
        //HttpHandler httpHandler = new HttpHandler();

        String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?"
                + "location=" + Data.getLatitude() + ',' + Data.getLongitude()
                + "&radius=" + RADIUS_IN_METERS
                + "&type=bar"
                + "&key=" + Data.getKEY();

        // Making a request to url and getting response
        //String jsonStr = httpHandler.makeServiceCall(url);

        Gson gson = new GsonBuilder().create();

        return null;//gson.fromJson(jsonStr, ListBars.class);
    }

    protected void onPostExecute(ListBars listBars) {

        listener.onBarsFetched(listBars);
        progressDialog.dismiss();

    }

    public interface OnBarsFetchedListener {
        void onBarsFetched(ListBars listBars);
    }

}
