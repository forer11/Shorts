package com.example.shortmaker;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shortmaker.DataClasses.Shortcut;

import java.io.Serializable;

public class SetActionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_actions);
        if(getIntent().getExtras() != null) {
            Bitmap bitmap = (Bitmap)this.getIntent().getParcelableExtra("shortcutIcon");
            String title = getIntent().getStringExtra("shortcutName");
            TextView shortcutTitle = findViewById(R.id.shortcutTitle);
            ImageView shortcutIcon = findViewById(R.id.shortcutIcon);
            shortcutTitle.setText(title);
            shortcutIcon.setImageBitmap(bitmap);
        }


    }
}