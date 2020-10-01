package com.example.shortmaker.DialogFragments;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shortmaker.ActionDialogs.ActionDialog;
import com.example.shortmaker.ActionFactory;
import com.example.shortmaker.Actions.Action;
import com.example.shortmaker.Adapters.ChooseActionAdapter;
import com.example.shortmaker.AppData;
import com.example.shortmaker.DataClasses.ActionObj;
import com.example.shortmaker.DataClasses.Icon;
import com.example.shortmaker.R;

import java.util.ArrayList;

public class ChooseActionDialog extends AppCompatDialogFragment implements ActionDialog.DialogListener {

    private Context context;

    @Override
    public void applyUserInfo(ArrayList<String> data) {
        //TODO - here we need to update the corresponding action with user input i.e - data
    }

    public interface ChooseActionDialogListener {
        void onChoseAction(Action action,int position);
    }

    // Use this instance of the interface to deliver action events
    ChooseActionDialogListener listener;

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
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

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Build the dialog and set up the button click handlers
        androidx.appcompat.app.AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.choose_action_dialog, null);
        setRecyclerView(view);
        builder.setTitle("Choose an action");
        builder.setView(view);
        return builder.create();
    }

    private void setRecyclerView(View view) {
        final ArrayList<ActionObj> items = ActionFactory.actionObjArrayList;
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        ChooseActionAdapter adapter = new ChooseActionAdapter(items);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        itemClickHandler(items, adapter);
    }

    private void itemClickHandler(final ArrayList<ActionObj> items, ChooseActionAdapter chooseActionAdapter) {
        chooseActionAdapter.setOnItemClickListener(new ChooseActionAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                dismiss();
                Action action = ActionFactory.getAction(items.get(position).getTitle());
                listener.onChoseAction(action,position);
            }
        });
    }
}
