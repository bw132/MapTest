package com.example.bdw16.maptest;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ListFragment extends DialogFragment {

    MapsActivity activity;

    ListFragment instance;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final View alertView = getActivity().getLayoutInflater().inflate(R.layout.fragment_list, null);
        builder.setView(alertView);


        builder.setTitle("Active Raids");

        builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });

        activity = (MapsActivity)getActivity();

        return builder.create();
    }

    public void createList() {
        LinearLayout layout = getDialog().findViewById(R.id.linearLayout);
        layout.removeAllViews();
        for (RaidLocation location : activity.getRaidManager().getActiveLocations()) {
            ViewGroup itemOuter = (ViewGroup) getActivity().getLayoutInflater().inflate(R.layout.list_item, null);
            final ViewGroup item = (ViewGroup) itemOuter.getChildAt(0);
            ((TextView)item.getChildAt(0)).setText(location.getTitle());
            ((TextView)item.getChildAt(1)).setText(location.getStateName());
            ((TextView)item.getChildAt(2)).setText("Interested: " + location.getTotalInterested());
            ((TextView)item.getChildAt(3)).setText("Going: " + location.getTotalGoing());

            final RaidLocation itemLocation = location;
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.currentMarker = itemLocation.getMarker();
                    new MarkerFragment().show(activity.getSupportFragmentManager(), "marker");
                }
            });

            layout.addView(itemOuter);
        }
    }

    private RaidLocation getRaidLocation() {
        MapsActivity activity = ((MapsActivity) getActivity());
        RaidLocation location = (RaidLocation)activity.currentMarker.getTag();
        return location;
    }

    @Override
    public void onStart() {
        super.onStart();
        createList();

        instance = this;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}