package com.example.bdw16.maptest;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Networker {

    private static final int RAID_LOCATION = 0x00;
    private static final int SEND_RAID_LOCATION = 0x01;
    private static final int REQUEST_RAID_LOCATION = 0x02;

    private static final int RAID_LOCATION_UPDATE = 0x10;

    private static final int RAIDER_UPDATE = 0x20;
    private static final int SEND_RAIDER_UPDATE = 0x21;
    private static final int REQUEST_RAIDER_UPDATE = 0x22;


    private static final int USERNAME_UPDATE = 0x30;
    private static final int REQUEST_USERNAME_UPDATE = 0x32;

    private static final int MESSAGE = 0x40;
    private static final int SEND_MESSAGE = 0x40;
    private static final int REQUEST_MESSAGE = 0x42;


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

    private static void requestData(String request) {
        connector.request(getUserCode() + N + request);
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
                case RAID_LOCATION_UPDATE:
                    receiveRaidUpdate(s);
                    break;
                case USERNAME_UPDATE:
                    receiveUsername(s);
                    break;
                case MESSAGE:
                    receiveMessage(s);
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
        receiveData(new Scanner(is).useDelimiter(N));
    }

    public static void sendRaidLocation(String title, LatLng position) {

        String data = SEND_RAID_LOCATION + N
                + (int)System.currentTimeMillis() + N
                + title + N
                + position.latitude + N
                + position.longitude + N;

        requestData(data);

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
        String dataBasic = SEND_RAIDER_UPDATE + N
                + location.getId() + N
                + raiderState + N;

        String extraData = "";
        if (location.isRaid) {
            extraData = location.getTime() + N
                    + location.getLevel() + N
                    + location.getType() + N;
        }

        String data = dataBasic + extraData;

        requestData(data);
    }

    public static void receiveRaiderUpdate(Scanner s) {
        int id = s.nextInt();
        int[] values = new int[4];
        for (int v=0;v<values.length;v++) values[v] = s.nextInt();
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
        String data = REQUEST_USERNAME_UPDATE + N
                + name + N;
        requestData(data);
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

    public static void sendMessage(RaidLocation location, String message) {
        String data = SEND_MESSAGE + N
                + location.getId() + N
                + message + N;
        requestData(data);
    }

    public static void requestMessage(RaidLocation location) {
        String data = REQUEST_MESSAGE + N
                + location.getId() + N;
        requestData(data);
    }

    public static void receiveMessage(Scanner data) {
        int id = data.nextInt();
        List<Message> messages = activity.getRaidManager().getLocation(id).getMessages();
        messages.clear();
        while (true) {
            String username = data.next();
            if (username.length() == 0) break;
            String text = data.next();
            messages.add(new Message(username, text));
        }
    }

}
