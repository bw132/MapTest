package com.example.bdw16.maptest;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;
import java.util.Scanner;

public class Networker {

    private static final int RAID_LOCATION = 1;
    private static final int RAIDER_UPDATE = 2;
    private static final int RAID_LOCATION_UPDATE = 3;
    private static final int RAID_LOCATION_REQUEST = 4;
    private static final int USERNAME_UPDATE = 5;


    private static MapsActivity activity;

    private static final String N = "\n";

    private static boolean useLocal = false;

    private static SocketConnector connector;

    private static Long userCode = null;
    private static String username = null;

    public static void registerMapsActivity(MapsActivity activity) {
        Networker.activity = activity;
        connector = new SocketConnector("35.189.37.118", 33001);
    }

    private static void sendData(String data) {
        if (useLocal) {
            receiveLocalData(data);
        }
        else {
            connector.send(getUserCode() + N + data);
        }
    }

    public static void receiveData(Scanner s) {

        while(s.hasNext()) {
            int type = s.nextInt();

            switch(type) {
                case RAID_LOCATION:
                    receiveRaidLocation(s);
                    break;
                case RAIDER_UPDATE:
                    receiveRaiderUpdate(s);
                    break;
                default:
                    break;
            }
        }

    }

    public static void receiveLocalData(final String data) {
        InputStream is = new InputStream() {
            int i = 0;
            char[] c = data.toCharArray();
            @Override
            public int read() throws IOException {
                if (i < c.length) {
                    i++;
                    return c[i-1];
                }
                return -1;
            }
        };
        receiveData(new Scanner(is));
    }

    public static void sendRaidLocation(String title, LatLng position) {

        String data = RAID_LOCATION + N
                + (int)System.currentTimeMillis() + N
                + title + N
                + position.latitude + N
                + position.longitude + N;

        sendData(data);

    }

    public static void receiveRaidLocation(Scanner s) {
        int id = s.nextInt();
        String name = s.next();
        double lat = s.nextDouble();
        double lng = s.nextDouble();

        if (activity.getRaidManager().hasLocation(id)) return;

        RaidLocation location = new RaidLocation(id, name, new LatLng(lat, lng));
        activity.getRaidManager().addLocation(location);
        location.initialiseMarker(activity);
    }

    public static void receiveRaidUpdate(Scanner s) {
        int id = s.nextInt();
        int state = s.nextInt();
        RaidLocation location = activity.getRaidManager().getLocation(id);
        location.setState(state);
        if (location.isRaid) {
            int time = s.nextInt();
            int level = s.nextInt();
            String type = s.nextLine();
            location.setRaidInfo(time, level, type);
        }
    }

    public static void sendRaiderUpdate(RaidLocation location, int raiderState) {
        String dataBasic = RAIDER_UPDATE + N
                + location.getId() + N
                + raiderState + N;

        String extraData = "";
        if (location.isRaid) {
            extraData = location.getTime() + N
                    + location.getLevel() + N
                    + location.getType() + N;
        }

        String data = dataBasic + extraData;

        sendData(data);
    }

    public static void receiveRaiderUpdate(Scanner s) {
        int id = s.nextInt();
        int[] values = new int[4];
        for (int v : values) v = s.nextInt();
        activity.getRaidManager().getLocation(id).setRaiders(values);
    }

    public static long getUserCode() {
        if (userCode != null) return userCode;

        Context context = activity.getApplicationContext();
        SharedPreferences sharedPref = context.getSharedPreferences("userCode", Context.MODE_PRIVATE);
        userCode = sharedPref.getLong("userCode", 0);
        if (userCode == 0) {
            userCode = new Random().nextLong();
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putLong("userCode", userCode);
            editor.apply();
        }

        return userCode;
    }

    public static void sendUsername(String name) {
        String data = USERNAME_UPDATE + N
                + name + N;
        sendData(data);
    }

    public static void receiveUsername(Scanner s) {
        String username = s.next();
        Networker.username = username;
    }

    public static String getUsername() {
        if (username != null) return username;

        Context context = activity.getApplicationContext();
        SharedPreferences sharedPref = context.getSharedPreferences("username", Context.MODE_PRIVATE);
        username = sharedPref.getString("username", null);
        if (username == null) {
            username = "";
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("username", username);
            editor.apply();
        }

        return username;
    }

}
