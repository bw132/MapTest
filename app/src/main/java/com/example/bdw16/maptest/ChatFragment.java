package com.example.bdw16.maptest;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

public class ChatFragment extends DialogFragment {

    public static ChatFragment instance;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final View alertView = getActivity().getLayoutInflater().inflate(R.layout.chat_fragment, null);
        builder.setView(alertView);


        builder.setTitle("Chat");

        builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });

        return builder.create();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();


    }

    private RaidLocation getRaidLocation() {
        MapsActivity activity = ((MapsActivity) getActivity());
        RaidLocation location = (RaidLocation)activity.currentMarker.getTag();
        return location;
    }

    public void update() {
        String text = "";
        for (Message m : getRaidLocation().getMessages()) {
            text += m.username + ": " + m.text + "\n\n";
        }
        ((EditText)getDialog().findViewById(R.id.textViewChat)).setText(text);
    }

    public static void updateInstance() {
        if (instance.isVisible()) {
            instance.update();
        }
    }



}