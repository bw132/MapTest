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

        builder.setPositiveButton("DONE", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });

        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();

        instance = this;

        getDialog().findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText edit = getDialog().findViewById(R.id.editText3);
                String text = edit.getText().toString().replace('\n', ' ');
                edit.setText("");
                Networker.sendMessage(getRaidLocation(), text);

                Networker.requestMessage(getRaidLocation());
            }
        });

        getDialog().findViewById(R.id.button4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText edit = getDialog().findViewById(R.id.editText5);
                String text = edit.getText().toString().replace('\n', ' ');
                edit.setText("");
                Networker.sendUsername(text);
                Networker.username = text;
                /*String text="";
                for (int i=0;i<20;i++) {
                    for (int n=0;n<i*4;n++) {
                        text+=".";
                    }
                    text+='\n';
                }*/
                //((TextView)getDialog().findViewById(R.id.textViewChat)).setText(text);


            }
        });

        Networker.requestMessage(getRaidLocation());

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
        ((TextView)getDialog().findViewById(R.id.textViewChat)).setText(text);
    }

    public static void updateInstance() {
        if (instance != null && instance.getActivity() != null && instance.getActivity().getSupportFragmentManager().findFragmentByTag("chat") != null) {
            instance.update();
        }
    }



}