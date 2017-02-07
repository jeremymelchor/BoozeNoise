package com.example.melchor.boozenoise.fragments;


import android.Manifest;
import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.melchor.boozenoise.Data;
import com.example.melchor.boozenoise.R;
import com.example.melchor.boozenoise.asynctasks.GetBarsAroundMe;
import com.example.melchor.boozenoise.entities.Bar;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import static android.content.ContentValues.TAG;
import static android.content.Context.LOCATION_SERVICE;

public class MapsFragment extends Fragment implements
        OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener,
        View.OnClickListener {

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
                } else if (BottomSheetBehavior.STATE_COLLAPSED == newState) {
                    itinerary.animate().scaleX(1).scaleY(1).setDuration(300).start();
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

        getBars = (com.getbase.floatingactionbutton.FloatingActionButton) view.findViewById(R.id.getBars);







        itinerary = (FloatingActionButton) view.findViewById(R.id.itinerary);
        itinerary.setVisibility(View.INVISIBLE);

        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewPageAndroid);
        ImageSliderAdapter imageSliderAdapter = new ImageSliderAdapter(getContext());
        viewPager.setAdapter(imageSliderAdapter);

        //Listeners
        view.findViewById(R.id.getBars).setOnClickListener(this);
        itinerary.setOnClickListener(this);

        // Gets the MapView from the XML layout and creates it
        mapView = (MapView) view.findViewById(R.id.maps);
        mapView.onCreate(savedInstanceState);


        // Gets the Map
        mapView.getMapAsync(this);

        // Set GPS permissions
        if (ContextCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_DENIED
                || ContextCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
            String[] permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
            ActivityCompat.requestPermissions(this.getActivity(), permissions, PackageManager.PERMISSION_GRANTED);
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
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
        }
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

    @Override
    public boolean onMarkerClick(Marker marker) {
        // 56 because it's the height of the bottom bar navigation
        bottomSheetBehavior.setPeekHeight(Math.round(Data.dpToPx(56 + 94)));
        itinerary.setVisibility(View.VISIBLE);
        getBars.setPadding(0,0,0,(int) Data.dpToPx(56));

        barSelected = (Bar) marker.getTag();

        // set bar name on bottom sheet peek
        TextView barName = (TextView) getView().findViewById(R.id.bottom_sheet_bar_name);
        barName.setText(barSelected.getName());
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.getBars:
                new GetBarsAroundMe(this.getContext(), Data.getLatitude(), Data.getLongitude(), Data.getRadiusInMeters(), map).execute();
                break;
            case R.id.itinerary:
                Uri googleMapsUri = Uri.parse("google.navigation:q="+barSelected.getVicinity());
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, googleMapsUri);
                startActivity(mapIntent);
                break;
        }
    }

    //==============================================================================================
    // Utils functions
    //==============================================================================================
}
