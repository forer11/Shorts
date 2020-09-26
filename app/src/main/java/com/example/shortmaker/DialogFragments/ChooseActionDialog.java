package com.example.shortmaker.DialogFragments;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shortmaker.ActionDialogs.ActionDialog;
import com.example.shortmaker.Actions.Action;
import com.example.shortmaker.Actions.ActionAlarmClock;
import com.example.shortmaker.Actions.ActionPhoneCall;
import com.example.shortmaker.Actions.ActionSendTextMessage;
import com.example.shortmaker.Actions.ActionSoundSettings;
import com.example.shortmaker.Actions.ActionSpotify;
import com.example.shortmaker.Actions.ActionWaze;
import com.example.shortmaker.Adapters.ChooseActionAdapter;
import com.example.shortmaker.R;

import java.util.ArrayList;

public class ChooseActionDialog extends AppCompatDialogFragment {

    private Context context;

    public interface ChooseActionDialogListener {
        void onDialogPositiveClick(DialogFragment dialog);
        void onDialogNegativeClick(DialogFragment dialog);
    }

    // Use this instance of the interface to deliver action events
    ChooseActionDialogListener listener;

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=context;
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            listener = (ChooseActionDialogListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(ChooseActionDialog.this.toString()
                    + " must implement NoticeDialogListener");
        }
    }


    private ArrayList<Action> createSampleData() {
        ArrayList<Action> items = new ArrayList<>();
        items.add(new ActionWaze(context));
        items.add(new ActionSpotify(context));
        items.add(new ActionAlarmClock(context));
        items.add(new ActionPhoneCall(context));
        items.add(new ActionSendTextMessage(context));
        items.add(new ActionSoundSettings(context));
        return items;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Build the dialog and set up the button click handlers
        androidx.appcompat.app.AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.choose_action_dialog,null);
        setRecyclerView(view);
        builder.setTitle("Choose an action");
        builder.setView(view);
        return builder.create();
    }

    private void setRecyclerView(View view) {
        final ArrayList<Action> items  = createSampleData();
        RecyclerView mRecyclerView = view.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
        ChooseActionAdapter mAdapter = new ChooseActionAdapter(items);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        itemClickHandler(items, mAdapter);
    }

    private void itemClickHandler(final ArrayList<Action> items, ChooseActionAdapter mAdapter) {
        mAdapter.setOnItemClickListener(new ChooseActionAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                dismiss();
                switch (items.get(position).getTitle()){
                        case "Waze action":
                            wazeActionHandler();
                            break;
                        case "Spotify action":
                            spotifyActionHandler();
                            break;
                        case "Set alarm clock action":
                            alarmClockActionHandler();
                            break;
                        case "Send text message action":
                            textMessageActionHandler();
                            break;
                        case "Sound settings action":
                            soundSettingActionHandler();
                            break;
                    }
            }
        });
    }

    private void soundSettingActionHandler() {
        ActionSoundSettings textMessage = new ActionSoundSettings(context);
        ActionDialog dialogFragment = textMessage.getDialog();
        if (dialogFragment != null) {
            dialogFragment.show(((FragmentActivity)context).getSupportFragmentManager(), "sound settings dialog");
        }
    }

    private void textMessageActionHandler() {
        ActionSendTextMessage textMessage = new ActionSendTextMessage(context);
        ActionDialog dialogFragment = textMessage.getDialog();
        if (dialogFragment != null) {
            dialogFragment.show(((FragmentActivity)context).getSupportFragmentManager(), "text message dialog");
        }
    }

    private void alarmClockActionHandler() {
        ActionAlarmClock alarmClock = new ActionAlarmClock(context);
        ActionDialog dialogFragment = alarmClock.getDialog();
        if (dialogFragment != null) {
            dialogFragment.show(((FragmentActivity)context).getSupportFragmentManager(), "alarm clock dialog");
        }
    }

    private void spotifyActionHandler() {
        ActionSpotify spotify = new ActionSpotify(context);
        ActionDialog dialogFragment = spotify.getDialog();
        if (dialogFragment != null) {
            dialogFragment.show(((FragmentActivity)context).getSupportFragmentManager(), "spotify dialog");
        }
    }

    private void wazeActionHandler() {
        ActionWaze waze = new ActionWaze(context);
        ActionDialog dialogFragment = waze.getDialog();
        if (dialogFragment != null) {
            dialogFragment.show(((FragmentActivity)context).getSupportFragmentManager(), "waze dialog");
        }
    }

}
