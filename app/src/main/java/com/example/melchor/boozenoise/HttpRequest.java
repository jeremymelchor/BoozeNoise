package com.example.melchor.boozenoise;



import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class HttpRequest implements Response.ErrorListener, Response.Listener<JSONObject> {

    private final String TAG = HttpRequest.class.getName();
    private Context context;
    private HttpRequestListener listener;

    public HttpRequest(Context context, HttpRequestListener listener) {
        this.context = context;
        this.listener = listener;
    }

    public void GET(String url) {
        Log.d(TAG,"url : "+url);
        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        queue.add(request);
    }

    //==============================================================================================
    // Volley events implementation
    //==============================================================================================

    @Override
    public void onErrorResponse(VolleyError error) {

    }

    @Override
    public void onResponse(JSONObject response) {
        try {
            listener.onGET(response);
        } catch (JSONException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    //==============================================================================================
    // Callback Interface
    //==============================================================================================

    public interface HttpRequestListener {
        void onGET(JSONObject response) throws JSONException, UnsupportedEncodingException;
    }
}
