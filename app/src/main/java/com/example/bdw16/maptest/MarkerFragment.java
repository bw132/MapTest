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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

public class MarkerFragment extends DialogFragment {

    public static MarkerFragment instance;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final View alertView = getActivity().getLayoutInflater().inflate(R.layout.fragment_marker, null);
        builder.setView(alertView);


        builder.setTitle(getRaidLocation().getMarker().getTitle());

        builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });

        return builder.create();
    }

    private RaidLocation getRaidLocation() {
        MapsActivity activity = ((MapsActivity) getActivity());
        RaidLocation location = (RaidLocation)activity.currentMarker.getTag();
        return location;
    }

    private void updateRaidState() {
        if (!getRaidLocation().isRaid) {
            getDialog().findViewById(R.id.constraintLayout).setVisibility(ConstraintLayout.VISIBLE);
            getDialog().findViewById(R.id.constraintLayout2).setVisibility(ConstraintLayout.GONE);
        }
        else {
            getDialog().findViewById(R.id.constraintLayout).setVisibility(ConstraintLayout.GONE);
            getDialog().findViewById(R.id.constraintLayout2).setVisibility(ConstraintLayout.VISIBLE);
        }
    }

    @Override
    public void onStart() {
        super.onStart();

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

        updateInfo();
        updateRaidState();
        updateRaiderState();

        String[] values = new String[] {"minutes until start", "minutes until end"};
        Spinner timeSpinner = getDialog().findViewById(R.id.spinner3);
        timeSpinner.setAdapter(new ArrayAdapter<>(getContext(), R.layout.adapter_text_view, values));

        String[] levels = new String[] {"Unknown", "1", "2", "3", "4", "5"};
        Spinner levelSpinner = getDialog().findViewById(R.id.spinner);
        levelSpinner.setAdapter(new ArrayAdapter<>(getContext(), R.layout.adapter_text_view_2, levels));

        String[] types = new String[] {"Unknown", "TempPokemon"};
        Spinner typeSpinner = getDialog().findViewById(R.id.spinner2);
        typeSpinner.setAdapter(new ArrayAdapter<>(getContext(), R.layout.adapter_text_view_3, types));

        getDialog().findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newRaid(v);
            }
        });

        getDialog().findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ChatFragment().show(getActivity().getSupportFragmentManager(), "chat");
            }
        });


        instance = this;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void updateInfo() {
        ((TextView)getDialog().findViewById(R.id.textView3)).setText("Time: " + getRaidLocation().getTimeString());
        ((TextView)getDialog().findViewById(R.id.textView4)).setText("Level: " + getRaidLocation().getLevel());
        ((TextView)getDialog().findViewById(R.id.textView5)).setText("Type: " + getRaidLocation().getType());
    }

    public void newRaid(View view) {
        String timeType = ((Spinner)getDialog().findViewById(R.id.spinner3)).getSelectedItem().toString();
        String level = ((Spinner)getDialog().findViewById(R.id.spinner)).getSelectedItem().toString();
        String type = ((Spinner)getDialog().findViewById(R.id.spinner2)).getSelectedItem().toString();
        String time = ((EditText)getDialog().findViewById(R.id.editText2)).getText().toString();

        if (level.equals("Unknown")) level = "0";
        if (time.equals("") || Integer.parseInt(time) > 60) time = "60";

        int state = timeType.endsWith("end") ? RaidLocation.STATE_ACTIVE : RaidLocation.STATE_PREPARED;

        Networker.sendRaidUpdate(getRaidLocation(), state, time, level, type);

    }

    public static void updateInstance() {
        if (instance != null && instance.getActivity() != null && instance.getActivity().getSupportFragmentManager().findFragmentByTag("marker") != null) {
            instance.updateRaidLocation();
            instance.updateRaiderState();
        }
    }

    public void updateRaidLocation() {
        updateRaidState();
        updateInfo();
    }

    public void updateRaiderState() {
        int[] raiders = getRaidLocation().getRaiders();

        String interested = "Interested";
        String going = "Going";
        String thereSoon = "There Soon";
        String ready = "Ready";

        if (raiders[RaidLocation.RAIDER_INTERESTED] > 0) interested += " (" + raiders[RaidLocation.RAIDER_INTERESTED] + ")";
        if (raiders[RaidLocation.RAIDER_GOING] > 0) going += " (" + raiders[RaidLocation.RAIDER_GOING] + ")";
        if (raiders[RaidLocation.RAIDER_THERE_SOON] > 0) thereSoon += " (" + raiders[RaidLocation.RAIDER_THERE_SOON] + ")";
        if (raiders[RaidLocation.RAIDER_READY] > 0) ready += " (" + raiders[RaidLocation.RAIDER_READY] + ")";

        ((RadioButton)getDialog().findViewById(R.id.radioButton)).setText(interested);
        ((RadioButton)getDialog().findViewById(R.id.radioButton2)).setText(going);
        ((RadioButton)getDialog().findViewById(R.id.radioButton3)).setText(thereSoon);
        ((RadioButton)getDialog().findViewById(R.id.radioButton4)).setText(ready);

    }

}