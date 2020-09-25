package com.example.shortmaker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shortmaker.DataClasses.Shortcut;
import com.example.shortmaker.Views.MovableFloatingActionButton;
import com.maltaisn.icondialog.IconDialog;
import com.maltaisn.icondialog.IconDialogSettings;
import com.maltaisn.icondialog.data.Icon;
import com.maltaisn.icondialog.pack.IconPack;

import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.List;

public class SetActionsActivity extends AppCompatActivity implements IconDialog.Callback {

    private static final String ICON_DIALOG_TAG = "icon-dialog";
    private ImageView shortcutIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_actions);
        if(getIntent().getExtras() != null) {
            Bitmap bitmap = (Bitmap)this.getIntent().getParcelableExtra("shortcutIcon");
            String title = getIntent().getStringExtra("shortcutName");
            TextView shortcutTitle = findViewById(R.id.shortcutTitle);
            shortcutIcon = findViewById(R.id.shortcutIcon);
            shortcutTitle.setText(title);
            shortcutIcon.setImageBitmap(bitmap);
        }
        MovableFloatingActionButton changeIconButton = findViewById(R.id.changeIcon);
        changeIconButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showIconPickerDialog();
            }
        });


    }

    private void showIconPickerDialog() {
        // If dialog is already added to fragment manager, get it. If not, create action new instance.
        IconDialog dialog = (IconDialog) getSupportFragmentManager().findFragmentByTag(ICON_DIALOG_TAG);
        IconDialog iconDialog = dialog != null ? dialog
                : IconDialog.newInstance(new IconDialogSettings.Builder().build());
        iconDialog.show(getSupportFragmentManager(), ICON_DIALOG_TAG);
    }


    @Nullable
    @Override
    public IconPack getIconDialogIconPack() {
        return ((AppData) getApplication()).getIconPack();
    }

    @Override
    public void onIconDialogIconsSelected(@NonNull IconDialog dialog, @NonNull List<Icon> icons) {
        shortcutIcon.setImageDrawable(icons.get(0).getDrawable());
    }


    @Override
    public void onIconDialogCancelled() {

    }
}