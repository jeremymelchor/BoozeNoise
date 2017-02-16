package com.example.melchor.boozenoise;


import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {

    private final String TAG = HttpRequest.class.getName();
    private Context context;
    private HttpRequestListener listener;

    public HttpRequest(Context context, HttpRequestListener listener) {
        this.context = context;
        this.listener = listener;
    }

    public void getBarsAroundMe() {
        RequestQueue queue = Volley.newRequestQueue(context);
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("latitude",Data.getLatitude());
            jsonBody.put("longitude",Data.getLongitude());
            jsonBody.put("radius",Data.getRadiusInMeters());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Data.getBarsAroundMeUrl(), jsonBody,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            listener.onBarsAroundMeFetched(response.getJSONArray("data"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()

                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", error.toString());
                    }
                }

        );
        request.setRetryPolicy(new DefaultRetryPolicy(14000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }

    //==============================================================================================
    // Volley events implementation
    //==============================================================================================


    //==============================================================================================
    // Callback Interface
    //==============================================================================================

    public interface HttpRequestListener {
        void onBarsAroundMeFetched(JSONArray response) throws JSONException;
    }
}
