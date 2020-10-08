package com.shorts.shortmaker.DialogFragments;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.shorts.shortmaker.ActionDialogs.ActionDialog;
import com.shorts.shortmaker.R;

import java.util.Objects;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class DeleteShortcutDialog extends ActionDialog {
    private Context context;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final AlertDialog.Builder builder =
                new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.delete_shortcut_dialog, null);
        ImageView imageView = view.findViewById(R.id.image_view);
        setButtons(view);
        setDialogImage(imageView, R.drawable.delete);
        builder.setCancelable(true);
        builder.setView(view)
                .setTitle("Are you sure you want to delete this Shortcut?");
        return builder.create();
    }

    private void setButtons(View view) {
        Button deleteButton, cancelButton;
        deleteButton = view.findViewById(R.id.delete_button);
        cancelButton = view.findViewById(R.id.cancel_button);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.onClick();
                dismiss();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    private CircularProgressDrawable setCircularProgressBar() {
        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(getContext());
        circularProgressDrawable.setStrokeWidth(5f);
        circularProgressDrawable.setCenterRadius(30f);
        circularProgressDrawable.start();
        return circularProgressDrawable;
    }

    protected void setDialogImage(ImageView imageView, int imagePath) {
        CircularProgressDrawable circularProgressDrawable = setCircularProgressBar();
        //Glide.with(this).load(R.drawable.alarm_clock_gif).placeholder(circularProgressDrawable).into(imageView);
        Glide.with(this).load(imagePath)
                .transition(withCrossFade())
                .placeholder(circularProgressDrawable)
                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                .into(imageView);
    }

}
