package com.shorts.shortmaker.DialogFragments;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.shorts.shortmaker.ActionDialogs.ActionDialog;
import com.shorts.shortmaker.ActionFactory;
import com.shorts.shortmaker.Adapters.ChooseActionAdapter;
import com.shorts.shortmaker.DataClasses.ActionData;
import com.shorts.shortmaker.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;

public class ChooseActionDialog extends AppCompatDialogFragment {

    private Context context;
    private ChooseActionAdapter adapter;

    public interface ChooseActionDialogListener {
        void onChoseAction(ActionData action, int position);
    }

    // Use this instance of the interface to deliver action events
    ChooseActionDialogListener listener;

    public void setNewChooseActionDialogListener(ChooseActionDialogListener listener) {
        this.listener = listener;
    }

    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Build the dialog and set up the button click handlers
        androidx.appcompat.app.AlertDialog.Builder builder = new AlertDialog
                .Builder(Objects.requireNonNull(getActivity()));
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.choose_action_dialog, null);
        setSearchEditText(view);
        setExitClickListener(view);
        setRecyclerView(view);
        builder.setView(view);
        return builder.create();
    }

    private void setExitClickListener(View view) {
        ImageButton exitButton = view.findViewById(R.id.exit_button);
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    private void setRecyclerView(View view) {
        final ArrayList<ActionData> items = new ArrayList<>(ActionFactory.ACTION_DATA_ARRAY_LIST);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        adapter = new ChooseActionAdapter(items);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        itemClickHandler(items, adapter);
    }

    private void itemClickHandler(final ArrayList<ActionData> items,
                                  ChooseActionAdapter chooseActionAdapter) {
        chooseActionAdapter.setOnItemClickListener(new ChooseActionAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                listener.onChoseAction(items.get(position), position);
                dismiss();
            }
        });
    }

    private void setSearchEditText(View view) {
        EditText searchEditText;
        searchEditText = view.findViewById(R.id.search_edit_text);
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                adapter.getFilter().filter(s.toString());
            }
        });
    }
}
