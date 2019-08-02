package com.example.bdw16.maptest;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class RaidLocation {

    public static final int STATE_MASK_RAID = 0x10;

    public static final int STATE_INITIAL = 0x0;
    public static final int STATE_READY = 0x0;
    public static final int STATE_PREPARED = 0x11;
    public static final int STATE_ACTIVE = 0x12;
    public static final int STATE_FINISHED = 0x13;

    public static final int RAIDER_UNSUBSCRIBED = -1;
    public static final int RAIDER_INTERESTED = 0;
    public static final int RAIDER_GOING = 1;
    public static final int RAIDER_THERE_SOON = 2;
    public static final int RAIDER_READY = 3;

    private int[] raiders = new int[4];

    private int id;
    private Marker marker;

    public boolean isRaid = false;
    private int state = STATE_READY;

    private int level = 0;
    private String type = "";
    private int time = 0;

    private String title = "";

    private LatLng position = null;

    private final List<Message> messages = new ArrayList<>();

    public RaidLocation(int id, String title, LatLng position) {
        this.id = id;
        this.title = title;
        this.position = position;
        setState(STATE_READY);//STATE_INITIAL
    }

    public Marker initialiseMarker(MapsActivity activity) {
        marker = activity.mMap.addMarker(new MarkerOptions()
                .draggable(false)
                .title(title)
                .position(position));
        marker.setTag(this);
        setState(state);
        return marker;
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
                setMarkerColor(BitmapDescriptorFactory.HUE_GREEN);
                break;
            case STATE_PREPARED:
                setMarkerColor(BitmapDescriptorFactory.HUE_ORANGE);
                break;
            case STATE_ACTIVE:
                setMarkerColor(BitmapDescriptorFactory.HUE_RED);
                break;
            case STATE_FINISHED:
                setMarkerColor(BitmapDescriptorFactory.HUE_VIOLET);
                break;
            default:
                setMarkerColor(BitmapDescriptorFactory.HUE_GREEN);
                break;
        }
        isRaid = (state & STATE_MASK_RAID) != 0;
        this.state = state;

    }

    public int getState() {
        return state;
    }

    public int[] getRaiders() {
        return raiders;
    }

    public void setRaiders(int[] raiders) {
        for (int r=0; r<raiders.length; r++) this.raiders[r] = raiders[r];
    }

    public void clearRaiders() {
        for (int r : raiders) r = 0;
    }

    public int getId() {
        return id;
    }

    public void setRaidInfo(int time, int level, String type) {
        this.level = level;
        this.type = type;
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public LatLng getPosition() {
        return position;
    }

    public void setMarkerColor(float color) {
        if (marker != null) marker.setIcon(BitmapDescriptorFactory.defaultMarker(color));
    }

    public int getLevel() {
        return level;
    }

    public String getType() {
        return type;
    }

    public int getTime() {
        return time;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public String getStateName() {
        switch (state) {
            case STATE_READY:
                return "Ready";
            case STATE_PREPARED:
                return "Prepared";
            case STATE_ACTIVE:
                return "Active";
            case STATE_FINISHED:
                return "Finished";
        }
        return "-";
    }

    public int getTotalInterested() {
        return raiders[0];
    }

    public int getTotalGoing() {
        return raiders[1] + raiders[2] + raiders[3];
    }

    public String getTimeString() {
        if (state == STATE_PREPARED) return time + " minutes until raid";
        else if (state == STATE_ACTIVE) return time + " minutes left for raid";
        else return "Raid finished";
    }

}
