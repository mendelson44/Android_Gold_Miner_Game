package com.example.gold_miner_game.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.gold_miner_game.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class MapFragment extends Fragment {


    private GoogleMap gMap;


    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        SupportMapFragment supportMapFragment = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.GoldMiner_fragmentMap_scoreBoard));
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {
                gMap = googleMap;
                LatLng location = new LatLng(55.6761, 12.5683);
                googleMap.addMarker(new MarkerOptions().position(location).title("copenhagen"));
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location,15));
            }
        });

        return view;
    }

   public void zoom(double lat , double lng){
       changeCamera(lat,lng);
    }

    public void changeCamera(double lat, double lng) {
        if (gMap != null) {
            LatLng location = new LatLng(lat, lng);
            gMap.clear(); // Clear existing markers
            gMap.addMarker(new MarkerOptions().position(location).title("New Location"));
            //gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(lat, lng))
                    .zoom(15)
                    .build();
            gMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        }
    }
}

