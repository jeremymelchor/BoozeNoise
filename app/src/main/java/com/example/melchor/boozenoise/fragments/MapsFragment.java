package com.example.melchor.boozenoise.fragments;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.melchor.boozenoise.Data;
import com.example.melchor.boozenoise.HttpRequest;
import com.example.melchor.boozenoise.R;
import com.example.melchor.boozenoise.entities.Bar;
import com.example.melchor.boozenoise.entities.ListBars;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import static android.content.Context.LOCATION_SERVICE;

public class MapsFragment extends Fragment implements
        OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener,
        View.OnClickListener,
        HttpRequest.HttpRequestListener {

    private final String TAG = MapsFragment.class.getName();

    private MapView mapView;
    private GoogleMap map;

    private BottomSheetBehavior bottomSheetBehavior;
    private FloatingActionButton itinerary;
    private com.getbase.floatingactionbutton.FloatingActionButton getBars;

    private Bar barSelected;

    // Http request variables
    private ArrayList<Bar> pendingListBar;

    //==============================================================================================
    // Lifecycle
    //==============================================================================================

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_maps, container, false);

        bottomSheetBehavior = BottomSheetBehavior.from(view.findViewById(R.id.bottom_sheet));
        bottomSheetBehavior.setPeekHeight(0);
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                // this part hides the button immediately
                if (BottomSheetBehavior.STATE_DRAGGING == newState) {
                    itinerary.animate().scaleX(0).scaleY(0).setDuration(300).start();
                    getBars.animate().scaleX(0).scaleY(0).setDuration(300).start();
                } else if (BottomSheetBehavior.STATE_COLLAPSED == newState) {
                    itinerary.animate().scaleX(1).scaleY(1).setDuration(300).start();
                    getBars.animate().scaleX(1).scaleY(1).setDuration(300).start();
                } else if (BottomSheetBehavior.STATE_EXPANDED == newState) {

                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        });



        /*com.getbase.floatingactionbutton.FloatingActionButton actionC = new com.getbase.floatingactionbutton.FloatingActionButton(getContext());
        actionC.setTitle("Hide/Show Action above");
        final FloatingActionsMenu menuMultipleActions = (FloatingActionsMenu) view.findViewById(R.id.multiple_actions);
        menuMultipleActions.addButton(actionC);
        final com.getbase.floatingactionbutton.FloatingActionButton actionA = (com.getbase.floatingactionbutton.FloatingActionButton) view.findViewById(R.id.action_a);
        actionA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actionA.setTitle("Action A clicked");
            }
        });*/

        getBars = (com.getbase.floatingactionbutton.FloatingActionButton) view.findViewById(R.id.get_bars);


        itinerary = (FloatingActionButton) view.findViewById(R.id.itinerary);
        itinerary.setVisibility(View.INVISIBLE);

        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewPageAndroid);
        ImageSliderAdapter imageSliderAdapter = new ImageSliderAdapter(getContext());
        viewPager.setAdapter(imageSliderAdapter);

        //Listeners
        view.findViewById(R.id.get_bars).setOnClickListener(this);
        view.findViewById(R.id.param).setOnClickListener(this);
        itinerary.setOnClickListener(this);

        // Gets the MapView from the XML layout and creates it
        mapView = (MapView) view.findViewById(R.id.maps);
        mapView.onCreate(savedInstanceState);


        // Gets the Map
        mapView.getMapAsync(this);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(this.getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this.getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            String[] permissions = new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION};
            ActivityCompat.requestPermissions(this.getActivity(), permissions, PackageManager.PERMISSION_GRANTED);
        }
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    //==============================================================================================
    // Events implementation
    //==============================================================================================

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (ActivityCompat.checkSelfPermission(this.getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this.getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            String[] permissions = new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION};
            ActivityCompat.requestPermissions(this.getActivity(), permissions, PackageManager.PERMISSION_GRANTED);
        } else {
            this.map = googleMap;
            map.setMyLocationEnabled(true);

            // Get LocationManager object from System Service LOCATION_SERVICE.
            // We need to call getActivity because fragment doesn't extends Context
            LocationManager locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);

            // Create a criteria object to retrieve provider
            Criteria criteria = new Criteria();

            // Get the name of the best provider
            String provider = locationManager.getBestProvider(criteria, true);

            // Get Current Location
            Location myLocation = locationManager.getLastKnownLocation(provider);

            double latitude = 43.696460;//myLocation.getLatitude();
            double longitude = 7.274179;//myLocation.getLongitude();

            Data.setLatitude(latitude);
            Data.setLongitude(longitude);

            // Move camera to current position
            LatLng latLng = new LatLng(latitude, longitude);
            map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            map.animateCamera(CameraUpdateFactory.zoomTo(15));

            // Set listener on marker clicks
            map.setOnMarkerClickListener(this);
        }

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        bottomSheetBehavior.setPeekHeight(Math.round(Data.dpToPx(94)));
        itinerary.setVisibility(View.VISIBLE);

        barSelected = (Bar) marker.getTag();

        // set bar name on bottom sheet peek
        TextView barName = (TextView) getView().findViewById(R.id.bottom_sheet_bar_name);
        barName.setText(barSelected.getName());

        // set rating bar on bottom sheet peek
        RatingBar ratingBar = (RatingBar) getView().findViewById(R.id.ratingBar);
        ratingBar.setRating(barSelected.getRating());
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.get_bars:
                pendingListBar = new ArrayList<>();
                new HttpRequest(getContext(), this).GET(Data.getBarsAroundMeUrl());
                break;
            case R.id.itinerary:
                Uri googleMapsUri = Uri.parse("google.navigation:q=" + barSelected.getVicinity());
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, googleMapsUri);
                startActivity(mapIntent);
                break;
            case R.id.param:
                BottomSheetDialogFragment bottomSheetDialogFragment = new BarsFetchParamFragment();
                bottomSheetDialogFragment.show(getFragmentManager(), bottomSheetDialogFragment.getTag());
        }
    }

    @Override
    public void onGET(JSONObject response) throws JSONException, UnsupportedEncodingException, MalformedURLException {
        Log.d(TAG, "response : " + response);
        Gson gson = new GsonBuilder().create();
        ListBars listBars = gson.fromJson(response.toString(), ListBars.class);
        pendingListBar.addAll(listBars.getResultsFromWebservice());
        Log.d(TAG, pendingListBar.toString());
        if (response.has("next_page_token")) {
            Log.d(TAG,response.getString("next_page_token"));
            String url = Data.getBarsAroundMeUrl() + "&pagetoken=";
            String next_token = response.getString("next_page_token");
            String url2 = url + next_token;
            Log.d(TAG,url2);
            new HttpRequest(getContext(), this).GET(url2);
        }
    }

    //==============================================================================================
    // Utils functions
    //==============================================================================================

}
