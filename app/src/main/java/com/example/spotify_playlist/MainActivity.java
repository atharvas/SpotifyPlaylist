package com.example.spotify_playlist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.spotify.sdk.android.player.Config;
import com.spotify.sdk.android.player.ConnectionStateCallback;
import com.spotify.sdk.android.player.Error;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.PlayerEvent;
import com.spotify.sdk.android.player.Spotify;
import com.spotify.sdk.android.player.SpotifyPlayer;

public class MainActivity extends Activity implements
        SpotifyPlayer.NotificationCallback, ConnectionStateCallback
{
    // TODO: Replace with your client ID//Done
    private static final String CLIENT_ID = "4712c40461f848f58a65ccd92acba7d2";

    // TODO: Replace with your redirect URI//Done
    private static final String REDIRECT_URI = "yourcustomprotocol://callback";

    private static final int REQUEST_CODE = 1337;


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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);

            switch (response.getType()) {
                // Response was successful and contains auth token
                case TOKEN:
                    // Handle successful response
                    Toast.makeText(getApplicationContext(), "oAuth successful!", Toast.LENGTH_SHORT).show();
                    Log.d("MainActivity", response.getAccessToken());
                    break;

                // Auth flow returned an error
                case ERROR:
                    // Handle error response
                    Toast.makeText(getApplicationContext(), "Trouble Contacting Spotify", Toast.LENGTH_SHORT).show();
                    Log.d("MainActivity", "Error.");

                    break;

                // Most likely auth flow was cancelled
                default:
                    Toast.makeText(getApplicationContext(), "You're already logged in!", Toast.LENGTH_SHORT).show();
                    Log.d("MainActivity", "Logged in");

                    // Handle other cases

            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onPlaybackEvent(PlayerEvent playerEvent) {
        Log.d("MainActivity", "Playback event received: " + playerEvent.name());
        switch (playerEvent) {
            // Handle event type as necessary
            default:
                break;
        }
    }

    @Override
    public void onPlaybackError(Error error) {
        Log.d("MainActivity", "Playback error received: " + error.name());
        switch (error) {
            // Handle error type as necessary
            default:
                break;
        }
    }

    @Override
    public void onLoggedIn() {
        Log.d("MainActivity", "User logged in");
    }

    @Override
    public void onLoggedOut() {
        Log.d("MainActivity", "User logged out");
    }

    @Override
    public void onLoginFailed(Error var1) {
        Log.d("MainActivity", "Login failed");
    }

    @Override
    public void onTemporaryError() {
        Log.d("MainActivity", "Temporary error occurred");
    }

    @Override
    public void onConnectionMessage(String message) {
        Log.d("MainActivity", "Received connection message: " + message);
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
        if (vibe != null) {
            vibe.vibrate(40);
        }
    }


    public void executeUAuth(android.view.View playlistCreate) {
        Toast.makeText(getApplicationContext(), "oAuth pressed", Toast.LENGTH_SHORT).show();
        Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (vibe != null) {
            vibe.vibrate(40);
        }

        //oAuth Activity
        AuthenticationRequest.Builder builder = new AuthenticationRequest.Builder(CLIENT_ID, AuthenticationResponse.Type.TOKEN, REDIRECT_URI);
        builder.setScopes(new String[]{"user-read-private", "streaming"});
        builder.setShowDialog(true);

        AuthenticationRequest request = builder.build();

        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);

    }
}

//package com.example.spotify_playlist;
//
//import android.app.Activity;
//import android.content.Context;
//import android.os.Bundle;
//import android.os.Vibrator;
//import android.util.AndroidException;
//import android.view.Window;
//import android.view.WindowManager;
//import android.widget.SeekBar;
//import android.widget.TextView;
//import android.widget.Toast;
//
//public class MainActivity extends Activity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        //Commands for Full-Screen
//        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        //Launch Activity Main Layout
//        this.setContentView(R.layout.activity_main);
//        onUpdate();
//
//
//
//
//    }
//

//}
