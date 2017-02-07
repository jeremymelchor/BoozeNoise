package com.example.melchor.boozenoise.asynctasks;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.util.Log;

import com.example.melchor.boozenoise.Data;
import com.example.melchor.boozenoise.R;
import com.example.melchor.boozenoise.entities.Bar;
import com.example.melchor.boozenoise.entities.ListBars;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GetBarsAroundMe extends AsyncTask<Void, Void, ListBars> {

    private static final String TAG = GetBarsAroundMe.class.getSimpleName();
    private double latitude, longitude;
    private int radius_in_meters;
    private GoogleMap googleMap;
    private Context context;

    public GetBarsAroundMe(Context context, double latitude, double longitude, int radius_in_meters, GoogleMap googleMap) {
        super();
        this.context = context;
        this.latitude = latitude;
        this.longitude = longitude;
        this.radius_in_meters = radius_in_meters;
        this.googleMap = googleMap;
    }

    @Override
    protected ListBars doInBackground(Void... voids) {
        HttpHandler httpHandler = new HttpHandler();

        String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?"
                + "location=" + latitude + ',' + longitude
                + "&radius=" + radius_in_meters
                + "&type=bar"
                + "&key=" + Data.getKEY();

        // Making a request to url and getting response
        String jsonStr = httpHandler.makeServiceCall(url);

        Gson gson = new GsonBuilder().create();
        ListBars listBars = gson.fromJson(jsonStr, ListBars.class);

        return listBars;
    }

    protected void onPostExecute(ListBars listBars) {
        Log.d(TAG, "WRITING TO DB..");
        // Persist bars found
        DatabaseManager databaseManager = new DatabaseManager("write");
        databaseManager.execute(listBars);

        // Update map
        for (Bar bar : listBars.getResultsFromWebservice()) {
            double decibel = bar.getDecibels();
            double latitude = bar.getGeometry().getLocation().getLatitude();
            double longitude = bar.getGeometry().getLocation().getLongitude();
            LatLng latLng = new LatLng(latitude, longitude);

            MarkerOptions marker = new MarkerOptions()
                    .position(latLng)
                    .title(bar.getName())
                    .icon(getMarkerIcon(decibel));

            googleMap.addMarker(marker);
        }
    }

    private BitmapDescriptor getMarkerIcon(double decibel) {
        Log.d(TAG,"decibel : "+decibel);
        BitmapDrawable bitmapdraw;
        if (decibel < 70)
            bitmapdraw = (BitmapDrawable) context.getResources().getDrawable(R.drawable.sound_low, null);
        else if (decibel >= 70 && decibel < 90)
            bitmapdraw = (BitmapDrawable) context.getResources().getDrawable(R.drawable.sound_medium, null);
        else
            bitmapdraw = (BitmapDrawable) context.getResources().getDrawable(R.drawable.sound_loud, null);

        Bitmap marker = Bitmap.createScaledBitmap(bitmapdraw.getBitmap(), 50, 50, false);
        return BitmapDescriptorFactory.fromBitmap(marker);
    }
}
