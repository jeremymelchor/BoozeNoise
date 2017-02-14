package com.example.melchor.boozenoise.asynctasks;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.melchor.boozenoise.Data;
import com.example.melchor.boozenoise.R;
import com.example.melchor.boozenoise.entities.Bar;
import com.example.melchor.boozenoise.entities.ListBars;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

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

        ArrayList<Bar> pendingListBar = new ArrayList<>();
        JSONObject jsonObj = new JSONObject();


        String url ="https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=43.69646,7.274179&radius=1000&type=bar&key=AIzaSyBKlwR3R7oovTzIh7xepPRrbIO6n-da6jQ&pagetoken=CoQC-wAAAMvo6PEa6q4LyMN_q-GpBfkuZPLn6_2Xl1XEJ5Ey-8AqIjZkPY1MnH2SaA_2hbQh-5GwafRFs13QJy5elLC6vEgA8KKCiaYne3NI2HGvNPxLmRR0q46iDJGEenq6nn1w1pwjrlRA35RTKSRgbBVkFIr8ASDdP8mEn7ddHushF5GM8PDUrV_5kH80JDRGJiPJ14V2T1gU2t5E38D9ZSN7ymf9_imu0tw0hY0ecB5HYubNoC9C--g8R0Hu7VnNgeUYogtEFKylK1LW4fmyIocPzv9ybtbwQQx9eFMP6XTfkPjWIASz6gA5MvDGrPO2Z_kUWypEKBuiVJW_iK7XoIYhBDkSEH4_G3HZOs6ZX-xLF4AliM0aFBZSZag0u_rTsP6aeb4LLsXma4Mr";
        RequestQueue queue = Volley.newRequestQueue(context);
        Log.d(TAG, "url : "+url);

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG,"response : "+ response);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        Log.d(TAG,"error : "+error);
                    }
                });
        queue.add(jsObjRequest);

        Log.d(TAG,"json request : "+jsObjRequest);


        /*do {
            *//*String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?"
                    + "location=" + latitude + ',' + longitude
                    + "&radius=" + radius_in_meters
                    + "&type=bar"
                    + "&key=" + Data.getKEY();
            if (Data.isOpenNow()) url += "&opennow";

            try {
                url += "&pagetoken="+jsonObj.getString("next_page_token");
            } catch (JSONException ignored) {}*//*

            String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=43.69646,7.274179&radius=1000&type=bar&key=AIzaSyBKlwR3R7oovTzIh7xepPRrbIO6n-da6jQ&pagetoken=CoQC-wAAAMvo6PEa6q4LyMN_q-GpBfkuZPLn6_2Xl1XEJ5Ey-8AqIjZkPY1MnH2SaA_2hbQh-5GwafRFs13QJy5elLC6vEgA8KKCiaYne3NI2HGvNPxLmRR0q46iDJGEenq6nn1w1pwjrlRA35RTKSRgbBVkFIr8ASDdP8mEn7ddHushF5GM8PDUrV_5kH80JDRGJiPJ14V2T1gU2t5E38D9ZSN7ymf9_imu0tw0hY0ecB5HYubNoC9C--g8R0Hu7VnNgeUYogtEFKylK1LW4fmyIocPzv9ybtbwQQx9eFMP6XTfkPjWIASz6gA5MvDGrPO2Z_kUWypEKBuiVJW_iK7XoIYhBDkSEH4_G3HZOs6ZX-xLF4AliM0aFBZSZag0u_rTsP6aeb4LLsXma4Mr";

            Log.d(TAG,"url length : "+url.length());
            Log.d(TAG, "url : " + url);

            // Making a request to url and getting response
            String jsonStr = httpHandler.makeServiceCall(url);

            Log.d(TAG, jsonStr);

            try {
                jsonObj = new JSONObject(jsonStr);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Gson gson = new GsonBuilder().create();
            ListBars listBars = gson.fromJson(jsonStr, ListBars.class);
            pendingListBar.addAll(listBars.getResultsFromWebservice());
            Log.d(TAG, pendingListBar.toString());
        } while (jsonObj.has("next_page_token"));*/

       /* ListBars listBars = new ListBars();
        listBars.setResultsFromWebservice(pendingListBar);*/

        //return listBars;
        return null;
    }

    protected void onPostExecute(ListBars listBars) {
        /*Log.d(TAG, "WRITING TO DB..");
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
                    .icon(getMarkerIcon(decibel));

            googleMap.addMarker(marker).setTag(bar);
        }*/
    }

    private BitmapDescriptor getMarkerIcon(double decibel) {
        Log.d(TAG, "decibel : " + decibel);
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
