package com.example.spotify_playlist;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.TextView;

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
}
