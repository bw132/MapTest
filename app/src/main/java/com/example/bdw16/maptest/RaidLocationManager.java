package com.example.bdw16.maptest;

import android.util.SparseArray;

import java.util.ArrayList;
import java.util.List;

public class RaidLocationManager {

    private SparseArray<RaidLocation> locations = new SparseArray<>();

    public RaidLocationManager() {

    }

    public void clearLocations() {
        locations = new SparseArray<>();
    }

    public void addLocation(RaidLocation location) {
        locations.put(location.getId(), location);
    }

    public void removeLocation(RaidLocation location) {
        locations.delete(location.getId());
    }

    public RaidLocation getLocation(int id) {
        return locations.get(id);
    }

    public boolean hasLocation(int id) {
        return getLocation(id) != null;
    }

    public List<RaidLocation> getActiveLocations() {
        List<RaidLocation> list = new ArrayList<>();
        for(int i = 0; i < locations.size(); i++) {
            int key = locations.keyAt(i);
            RaidLocation l = locations.get(key);
            if (l.isRaid) list.add(l);
        }
        return list;
    }

}
