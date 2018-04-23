package com.example.spotify_playlist;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.AndroidException;
import android.view.Window;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Commands for Full-Screen
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //Launch Activity Main Layout
        this.setContentView(R.layout.activity_main);
        onUpdate();




    }

    private void onUpdate() {
        SeekBar getHourSeeker = (SeekBar)findViewById(R.id.hourBar);
        SeekBar getMinuteSeeker = (SeekBar)findViewById(R.id.minuteBar);
        final TextView putHourText = (TextView)findViewById(R.id.dynamic_playlistHRS);
        final TextView putMinuteText = (TextView)findViewById(R.id.dynamic_playlistMIN);
        putHourText.setText(String.valueOf(0));
        putMinuteText.setText(String.valueOf(0));

        getHourSeeker.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                putHourText.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        getMinuteSeeker.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                putMinuteText.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


    }
    public void executeProgram(android.view.View playlistCreate) {
        Toast.makeText(getApplicationContext(), "playlistCreate pressed", Toast.LENGTH_SHORT).show();
        Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (vibe != null)
            vibe.vibrate(40);
    }


    public void executeUAuth(android.view.View playlistCreate) {
        Toast.makeText(getApplicationContext(), "UAuth pressed", Toast.LENGTH_SHORT).show();
        Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (vibe != null)
            vibe.vibrate(40);
    }
}
