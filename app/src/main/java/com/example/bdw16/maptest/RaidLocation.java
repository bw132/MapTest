package com.example.bdw16.maptest;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class RaidLocation {

    private static final int STATE_MASK_RAID = 0x10;

    private static final int STATE_READY = 0x1;
    private static final int STATE_PREPARED = 0x11;
    private static final int STATE_ACTIVE = 0x12;
    private static final int STATE_FINISHED = 0x13;

    public int RAIDER_INTERESTED = 0;
    public int RAIDER_GOING = 1;
    public int RAIDER_THERE_SOON = 2;
    public int RAIDER_READY = 3;

    private int[] raiders = new int[4];

    private int id;
    private Marker marker;

    public boolean isRaid = false;

    public RaidLocation(int id, Marker marker) {
        this.id = id;
        this.marker = marker;
        setState(STATE_READY);

        marker.setTag(this);
//        marker = new MarkerOptions().position(latLng)
//                .title(name)
//                .draggable(false)
//                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
    }

    public void prepareRaid() {

        //TODO From start time
    }

    public void startRaid() {
        //TODO from time left
    }

    public Marker getMarker() {
        return marker;
    }

    public void setState(int state) {
        switch (state) {
            case STATE_READY:
                marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                break;
            case STATE_PREPARED:
                marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
                break;
            case STATE_ACTIVE:
                marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                break;
            case STATE_FINISHED:
                marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));
                break;
        }
        isRaid = (state & STATE_MASK_RAID) == 0;

    }

    public int[] getRaiders() {
        return raiders;
    }

    public void clearRaiders() {
        for (int r : raiders) r = 0;
    }

}
