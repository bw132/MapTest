package com.example.bdw16.maptest;

import java.util.ArrayList;
import java.util.List;

public class RaidLocationManager {

    private List<RaidLocation> locations = new ArrayList<>();

    public RaidLocationManager() {

    }

    public void clearLocations() {
        locations = new ArrayList<>();
    }

    public void addLocation(RaidLocation location) {
        locations.add(location);
    }

    public void removeLocation(RaidLocation location) {
        locations.remove(location);
    }

}
