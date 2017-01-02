package com.example.melchor.boozenoise.fragments;


import android.Manifest;
import android.app.Fragment;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.melchor.boozenoise.DataCommunication;
import com.example.melchor.boozenoise.R;
import com.example.melchor.boozenoise.utils.GetBarsAroundMe;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import static android.content.ContentValues.TAG;
import static android.content.Context.LOCATION_SERVICE;

public class MapsFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private MapView mapView;
    private GoogleMap map;
    private DataCommunication dataCommunication;

    private BottomSheetBehavior bottomSheetBehavior;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_maps, container, false);

        bottomSheetBehavior = BottomSheetBehavior.from(view.findViewById(R.id.fragment_bottom_sheet));
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

        // Gets the MapView from the XML layout and creates it
        mapView = (MapView) view.findViewById(R.id.maps);
        mapView.onCreate(savedInstanceState);

        // Gets the Map
        mapView.getMapAsync(this);

        // Set GPS permissions
        if (ContextCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_DENIED
                || ContextCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
            String[] permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION};
            ActivityCompat.requestPermissions(this.getActivity(), permissions, PackageManager.PERMISSION_GRANTED);
        }

        view.findViewById(R.id.getBars).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new GetBarsAroundMe(dataCommunication.getLatitude(), dataCommunication.getLongitude(), dataCommunication.getRadiusInMeters(), map).execute();
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            dataCommunication = (DataCommunication) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement DataCommunication");
        }
    }

    /**************************************/
    /**              EVENTS              **/
    /**************************************/

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

        dataCommunication.setLatitude(latitude);
        dataCommunication.setLongitude(longitude);

        // Move camera to current position
        LatLng latLng = new LatLng(latitude, longitude);
        map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        map.animateCamera(CameraUpdateFactory.zoomTo(15));

        // Set listener on marker clicks
        map.setOnMarkerClickListener(this);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        bottomSheetBehavior.setPeekHeight(300);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        Log.d(TAG, marker.getTitle());
        return false;
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

    /**************************************/
    /**            FUNCTIONS             **/
    /**************************************/
}
