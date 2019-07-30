package com.example.bdw16.maptest;

public class Networker {

    private static MapsActivity activity;

    public static void registerMapsActivity(MapsActivity activity) {
        Networker.activity = activity;
    }

    private static void sendData() {

    }

    public static void sendRaidLocation(RaidLocation location) {
        //TODO
    }

    public static void receiveRaidLocation(String data) {

    }

    public static void sendRaiderUpdate(RaidLocation location, int raiderState) {

    }

}
