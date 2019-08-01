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
import android.widget.RadioGroup;

public class MarkerFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final View alertView = getActivity().getLayoutInflater().inflate(R.layout.fragment_marker, null);
        builder.setView(alertView);


        builder.setTitle(getRaidLocation().getMarker().getTitle());

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });

        return builder.create();
    }

    private RaidLocation getRaidLocation() {
        MapsActivity activity = ((MapsActivity) getActivity());
        RaidLocation location = (RaidLocation)activity.currentMarker.getTag();
        return location;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getRaidLocation().isRaid) {
            getDialog().findViewById(R.id.constraintLayout).setVisibility(ConstraintLayout.VISIBLE);
            getDialog().findViewById(R.id.constraintLayout2).setVisibility(ConstraintLayout.GONE);
        }
        else {
            getDialog().findViewById(R.id.constraintLayout).setVisibility(ConstraintLayout.GONE);
            getDialog().findViewById(R.id.constraintLayout2).setVisibility(ConstraintLayout.VISIBLE);
        }

        RadioGroup radioGroup = (RadioGroup) getDialog().findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int state;
                switch(checkedId) {
                    case R.id.radioButton:
                        state = RaidLocation.RAIDER_INTERESTED;
                        break;
                    case R.id.radioButton2:
                        state = RaidLocation.RAIDER_GOING;
                        break;
                    case R.id.radioButton3:
                        state = RaidLocation.RAIDER_THERE_SOON;
                        break;
                    case R.id.radioButton4:
                        state = RaidLocation.RAIDER_READY;
                        break;
                    default:
                        state = RaidLocation.RAIDER_UNSUBSCRIBED;
                        break;
                }
                Networker.sendRaiderUpdate(getRaidLocation(), state);
            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void chooseRaiderState(View view) {

    }

    public void updateInfo() {

    }

}