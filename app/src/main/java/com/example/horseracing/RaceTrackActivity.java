package com.example.horseracing;

import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class RaceTrackActivity extends AppCompatActivity {
    private MediaPlayer idleSound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_race_track);

        idleSound = MediaPlayer.create(this, R.raw.idle_sound);
        idleSound.setLooping(true);
        idleSound.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Giải phóng tài nguyên MediaPlayer
        if (idleSound != null) {
            idleSound.stop();
            idleSound.release();
            idleSound = null;
        }
    }
}