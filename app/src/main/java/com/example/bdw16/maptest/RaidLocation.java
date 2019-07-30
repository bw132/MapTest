package com.example.bdw16.maptest;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class RaidLocation {

    private int id;
    private String name;
    private MarkerOptions options;

    public RaidLocation(int id, String name, LatLng latLng) {
        this.id = id;
        this.name = name;
        options = new MarkerOptions().position(latLng)
                .title(name)
                .draggable(false)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
    }

    public void prepareRaid() {
        //TODO From start time
    }

    public void startRaid() {
        //TODO from time left
    }

}
