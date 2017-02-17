package com.example.melchor.boozenoise.fragments;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
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
import com.example.melchor.boozenoise.asynctasks.DatabaseManager;
import com.example.melchor.boozenoise.entities.Bar;
import com.example.melchor.boozenoise.entities.ListBars;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import org.json.JSONArray;
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
        HttpRequest.HttpRequestListener,
        DatabaseManager.OnBarStored {

    private final String TAG = MapsFragment.class.getName();

    private MapView mapView;
    private GoogleMap map;

    private BottomSheetBehavior bottomSheetBehavior;
    private FloatingActionButton itinerary;
    private com.getbase.floatingactionbutton.FloatingActionButton getBars;

    private Bar barSelected;

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

    /**
     * Check the permission for the location
     */
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

    /**
     * When the map is ready to be manipulated
     *
     * @param googleMap
     */
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

    /**
     * Set information display when we click on a marker
     *
     * @param marker
     * @return
     */
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

        // set sound level on bottom sheet peek
        TextView noise = (TextView) getView().findViewById(R.id.bottom_sheet_noise);
        noise.setText(getBarsSoundLevel(barSelected.getDecibels()));
        return false;
    }

    /**
     * Click events
     *
     * @param v the view we clicked
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.get_bars:
                new HttpRequest(getContext(), this).getBarsAroundMe(Data.getRadiusInMeters());
                map.clear();
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

    /**
     * Fired when we fetched the bars around us
     *
     * @param response from the webservice
     * @throws JSONException
     */
    @Override
    public void onBarsAroundMeFetched(JSONArray response) throws JSONException {
        ArrayList<Bar> pendingListBar = new ArrayList<>();

        // create all Bar objects with webservice response
        for (int i = 0; i < response.length(); i++) {
            Gson gson = new GsonBuilder().create();
            ListBars listBars = gson.fromJson(response.get(i).toString(), ListBars.class);
            pendingListBar.addAll(listBars.getResultsFromWebservice());
        }

        final ListBars listBars = new ListBars();
        listBars.setResultsFromWebservice(pendingListBar);
        // Persist bars found
        DatabaseManager databaseManager = new DatabaseManager("write", this);
        databaseManager.execute(listBars);
    }

    /**
     * When we updated the database, we retrieve the bars newly added and we
     * fire this event so we can treat our datas. Here we put markers on the map
     */
    @Override
    public void onBarStored() {
        for (int i = 0; i < Data.getCurrentListBars().size(); i++) {
            double decibel = Data.getCurrentListBars().get(i).getDecibels();
            double latitude = Data.getCurrentListBars().get(i).getGeometry().getLocation().getLatitude();
            double longitude = Data.getCurrentListBars().get(i).getGeometry().getLocation().getLongitude();
            LatLng latLng = new LatLng(latitude, longitude);

            MarkerOptions marker = new MarkerOptions()
                    .position(latLng)
                    .icon(getMarkerIcon(decibel));

            map.addMarker(marker).setTag(Data.getCurrentListBars().get(i));
        }
    }


    //==============================================================================================
    // Utils functions
    //==============================================================================================

    /**
     * Determine which icon we use for a merker depending on decibel
     *
     * @param decibel decibels of a bar
     * @return a BitmapDescriptor describing the marker icon
     */
    private BitmapDescriptor getMarkerIcon(double decibel) {
        Log.d(TAG, "decibel : " + decibel);
        BitmapDrawable bitmapdraw;
        if (decibel < 70)
            bitmapdraw = (BitmapDrawable) getContext().getResources().getDrawable(R.drawable.sound_low, null);
        else if (decibel >= 70 && decibel < 90)
            bitmapdraw = (BitmapDrawable) getContext().getResources().getDrawable(R.drawable.sound_medium, null);
        else
            bitmapdraw = (BitmapDrawable) getContext().getResources().getDrawable(R.drawable.sound_loud, null);

        Bitmap marker = Bitmap.createScaledBitmap(bitmapdraw.getBitmap(), 50, 50, false);
        return BitmapDescriptorFactory.fromBitmap(marker);
    }

    private String getBarsSoundLevel(double decibel) {
        String result;
        if (decibel < 70)
            result = "Peu bruyant";
        else if (decibel >= 70 && decibel < 90)
            result = "Son modéré";
        else
            result = "très bruyant";

        return result;
    }
}
