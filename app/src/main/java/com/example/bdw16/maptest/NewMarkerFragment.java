package com.example.bdw16.maptest;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class NewMarkerFragment extends DialogFragment {

    public NewMarkerFragment() {}

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final View alertView = getActivity().getLayoutInflater().inflate(R.layout.new_fragment_marker, null);
        builder.setView(alertView);
        builder.setTitle(R.string.new_marker);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //TODO Push marker to server
                //TEMP:
                MapsActivity activity = ((MapsActivity) getActivity());
                Marker marker = activity.mMap.addMarker(new MarkerOptions()
                        .draggable(false)
                        .title(((EditText)alertView.findViewById(R.id.editText)).getText().toString())
                        .position(activity.newMarker.getPosition()));
                activity.newMarker.remove();
                activity.newMarker = null;

                RaidLocation location = new RaidLocation(-1, marker);
                activity.getRaidManager().addLocation(location);
                //KEEP
                Networker.sendRaidLocation(location);
            }
        });


        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });

        return builder.create();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}