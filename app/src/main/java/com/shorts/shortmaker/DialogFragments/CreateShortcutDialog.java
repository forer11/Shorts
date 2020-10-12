package com.shorts.shortmaker.DialogFragments;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.shorts.shortmaker.Activities.SetActionsActivity;
import com.shorts.shortmaker.AppData;
import com.shorts.shortmaker.DataClasses.Shortcut;
import com.shorts.shortmaker.FireBaseHandlers.FireStoreHandler;
import com.shorts.shortmaker.R;

import java.util.Objects;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;
import static com.facebook.FacebookSdk.getApplicationContext;

public class CreateShortcutDialog extends AppCompatDialogFragment {

    private Context context;
    private int pos;
    private TextWatcher userInputTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (shortcutTitleEditText.getText().toString().equals("")) {
                shortcutTitleEditText.setError("Invalid name");
                okButton.setEnabled(false);
            } else {
                shortcutTitleEditText.setError(null);
                okButton.setEnabled(true);
            }
        }
    };
    private EditText shortcutTitleEditText;
    private Button okButton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pos = Objects.requireNonNull(getArguments()).getInt("pos");
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.create_shortcut_dialog, null);
        final AppData appData = (AppData) getApplicationContext();
        shortcutTitleEditText = view.findViewById(R.id.shortcutTitle);
        shortcutTitleEditText.addTextChangedListener(userInputTextWatcher);
        setDialogButtons(view, appData, shortcutTitleEditText);
        builder.setView(view)
                .setTitle("Add a shortcut");
        return builder.create();
    }

    protected void setDialogButtons(View view, final AppData appData, final EditText shortcutTitleEditText) {
        okButton = view.findViewById(R.id.okButton);
        Button cancelButton = view.findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addShortcut(shortcutTitleEditText.getText().toString(), appData);

            }
        });
    }

    protected void addShortcut(String title, AppData appData) {
        Shortcut shortcut = new Shortcut(title,
                FireStoreHandler.DEFAULT_IMAGE_URL);
        shortcut.setPos(pos);
        appData.fireStoreHandler.addShortcut(shortcut,
                new FireStoreHandler.SingleShortcutCallback() {
                    @Override
                    public void onAddedShortcut(String id,
                                                Shortcut shortcut1,
                                                Boolean success) {
                        if (success) { //TODO if not successful
                            Intent intent = new Intent(context,
                                    SetActionsActivity.class);
                            intent.putExtra("shortcutId", id);
                            context.startActivity(intent);
                            dismiss();
                        }
                    }
                });
    }

}
