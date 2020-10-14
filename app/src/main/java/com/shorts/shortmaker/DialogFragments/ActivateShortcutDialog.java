package com.shorts.shortmaker.DialogFragments;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.shorts.shortmaker.AppData;
import com.shorts.shortmaker.R;

import static com.facebook.FacebookSdk.getApplicationContext;

public class ActivateShortcutDialog extends AppCompatDialogFragment {

    public static final String USER_MSG = "activate this shortcut?";
    private Context context;
    private DialogListener listener;
    private Button okButton;
    private boolean checked = false;

    public interface DialogListener {
        void getResponse(Boolean show, boolean activate);
    }

    public void setNewDialogListener(DialogListener listener) {
        this.listener = listener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        View view = layoutInflater.inflate(R.layout.activate_shortcut, null);
        TextView textView = view.findViewById(R.id.shortcutTitle);
        textView.setText(USER_MSG);
        final AppData appData = (AppData) getApplicationContext();
        setDialogButtons(view, appData);
        builder.setView(view)
                .setTitle("Activation");
        return builder.create();
    }

    protected void setDialogButtons(View view, final AppData appData) {
        okButton = view.findViewById(R.id.okButton);
        Button cancelButton = view.findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.getResponse(checked, false);
                dismiss();
            }
        });
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.getResponse(checked, true);
                dismiss();
            }
        });

        CheckBox checkBox = view.findViewById(R.id.show_checkbox);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checked = isChecked;
            }
        });
    }


}
