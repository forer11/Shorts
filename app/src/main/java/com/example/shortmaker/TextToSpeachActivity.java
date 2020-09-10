package com.example.shortmaker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;

import java.util.Locale;

public class TextToSpeachActivity extends AppCompatActivity {

    private TextToSpeech textToSpeech;
    private EditText editText;
    private SeekBar seekBarPitch;
    private SeekBar seekBarSpeed;
    private Button buttonSpeak;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_to_speach);

        buttonSpeak = findViewById(R.id.button_speak);

        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = textToSpeech.setLanguage(Locale.ENGLISH);

                    if (result == TextToSpeech.LANG_MISSING_DATA
                            || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Toast.makeText(TextToSpeachActivity.this, "Language not supported", Toast.LENGTH_SHORT).show();
                    } else {
                        //everything went well
                        buttonSpeak.setEnabled(true);
                    }
                } else {
                    Toast.makeText(TextToSpeachActivity.this, "Initialization failed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        editText = findViewById(R.id.edit_text);
        seekBarPitch = findViewById(R.id.seek_bar_pitch);
        seekBarSpeed = findViewById(R.id.seek_bar_speed);

        buttonSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speak();
            }
        });
    }


    private void speak() {
        String text = editText.getText().toString();
        float pitch = (float) seekBarPitch.getProgress() / 50;
        if(pitch < 0.1) {
            pitch = 0.1f;
        }
        float speed = (float) seekBarSpeed.getProgress() / 50;
        if(speed < 0.1) {
            speed = 0.1f;
        }

        textToSpeech.setPitch(pitch);
        textToSpeech.setSpeechRate(speed);

        textToSpeech.speak(text,TextToSpeech.QUEUE_FLUSH,null);
    }

    @Override
    protected void onDestroy() {
        if(textToSpeech!=null) {
            textToSpeech.stop();
            textToSpeech.shutdown();

        }
        super.onDestroy();
    }
}