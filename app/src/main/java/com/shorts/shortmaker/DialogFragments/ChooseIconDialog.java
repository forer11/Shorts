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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.shorts.shortmaker.Adapters.ChooseIconAdapter;
import com.shorts.shortmaker.AppData;
import com.shorts.shortmaker.DataClasses.Icon;
import com.shorts.shortmaker.R;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class ChooseIconDialog extends AppCompatDialogFragment {

    private Context context;
    private OnIconPick callback;
    private EditText searchEditText;
    private ChooseIconAdapter adapter;

    public interface OnIconPick {
        public void onIconPick(String iconLink);
    }

    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
        this.context = context;

        try {
            callback = (OnIconPick) context;
        } catch (ClassCastException e) {
            throw new ClassCastException("Calling Fragment must implement OnIconPick");
        }
    }

    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Build the dialog and set up the button click handlers
        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.choose_icon_dialog, null);
        setRecyclerView(view);
        builder.setView(view);
        setSearchEditText(view);
        setExitButton(view);
        return builder.create();
    }

    private void setExitButton(View view) {
        ImageButton exitButton = view.findViewById(R.id.exit_button);
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    private void setSearchEditText(View view) {
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

    private void setRecyclerView(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        adapter = new ChooseIconAdapter(context, (AppData) context.getApplicationContext());
        recyclerView.setLayoutManager(new GridLayoutManager(context, 3));
        recyclerView.setAdapter(adapter);
        iconClickHandler(adapter);
    }

    private void iconClickHandler(ChooseIconAdapter adapter) {
        adapter.setOnIconClickListener(new ChooseIconAdapter.OnIconClickListener() {
            @Override
            public void onIconClick(int position) {
                Icon icon = ((AppData) context.getApplicationContext()).getIcons().get(position);
                callback.onIconPick(icon.getLink());
                dismiss();
            }
        });
    }
}
