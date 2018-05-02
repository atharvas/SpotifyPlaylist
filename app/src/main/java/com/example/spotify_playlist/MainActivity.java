package com.example.spotify_playlist;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
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

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Random;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyCallback;
import kaaes.spotify.webapi.android.SpotifyError;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Pager;
import kaaes.spotify.webapi.android.models.Playlist;
import kaaes.spotify.webapi.android.models.PlaylistBase;
import kaaes.spotify.webapi.android.models.PlaylistSimple;
import kaaes.spotify.webapi.android.models.PlaylistTrack;
import kaaes.spotify.webapi.android.models.Recommendations;
import kaaes.spotify.webapi.android.models.Result;
import kaaes.spotify.webapi.android.models.SavedTrack;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.TrackToRemove;
import kaaes.spotify.webapi.android.models.TracksToRemove;
import kaaes.spotify.webapi.android.models.UserPrivate;
import kaaes.spotify.webapi.android.models.UserPublic;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

//https://goo.gl/eST8wY - Link to github
public class MainActivity extends Activity implements
        SpotifyPlayer.NotificationCallback, ConnectionStateCallback
{
    private static final String CLIENT_ID = "4712c40461f848f58a65ccd92acba7d2";

    private static final String REDIRECT_URI = "yourcustomprotocol://callback";

    private static final int REQUEST_CODE = 1337;

    private static String ACCESS_TOKEN = "";

    private int PLAYLIST_HRS;

    private int PLAYLIST_MINS;

    private long PLAYLIST_DURATION;



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
                    Log.d("MainActivity", "oAuth successful: " + response.getAccessToken());
                    ACCESS_TOKEN = response.getAccessToken();
                    break;

                // Auth flow returned an error
                case ERROR:
                    // Handle error response
                    Toast.makeText(getApplicationContext(), "oAuth failed!", Toast.LENGTH_SHORT).show();
                    Log.d("MainActivity", "Error.");
                    break;

                // Most likely auth flow was cancelled
                default:
                    Toast.makeText(getApplicationContext(), "oAuth failed!", Toast.LENGTH_SHORT).show();
                    Log.d("MainActivity", "oAuth failed");

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
                PLAYLIST_HRS = progress;
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
                PLAYLIST_MINS = progress;

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
        if (ACCESS_TOKEN.equals("")) {
            Toast.makeText(getApplicationContext(), "You're not logged in.", Toast.LENGTH_SHORT).show();
        } else if (PLAYLIST_MINS == 0 && PLAYLIST_HRS == 0) {
            Toast.makeText(getApplicationContext(), "Please select a valid time.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "EXECTUTE Fn", Toast.LENGTH_SHORT).show();
            PlaylistGeneration();
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
        builder.setScopes(new String[]{"user-read-private", "streaming", "user-library-read", "playlist-modify-public", "playlist-modify-private", "user-read-private"});
        builder.setShowDialog(true);

        AuthenticationRequest request = builder.build();

        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);

    }

    private void PlaylistGeneration() {
        Toast.makeText(getApplicationContext(), "PlayGen Called.", Toast.LENGTH_SHORT).show();
        PLAYLIST_DURATION = (PLAYLIST_HRS * 60 * 60 * 1000 + PLAYLIST_MINS * 60 * 1000);
        SpotifyApi songs = new SpotifyApi();
        songs.setAccessToken(ACCESS_TOKEN);
        String offset = "0";
        String limit = "30";
        Map<String, Object> options = new HashMap<>();
        options.put(SpotifyService.OFFSET, offset);
        options.put(SpotifyService.LIMIT, limit);

        songs.getService().getMySavedTracks(options, new SpotifyCallback<Pager<SavedTrack>>() {
            @Override
            public void success(Pager<SavedTrack> savedTrackPager, Response response) {
                // handle successful response
                Log.d("MainActivity", String.valueOf(savedTrackPager.items.size()));
                Parser(savedTrackPager.items);
            }

            @Override
            public void failure(SpotifyError spotifyError) {
                // handle error
                Log.d("MainActivity", spotifyError.toString());
                Toast.makeText(getApplicationContext(), "getMySavedTracks" + spotifyError.toString(), Toast.LENGTH_SHORT).show();
                return;

            }
        });
//        try {
//            Pager<SavedTrack> mySavedTracks = songs.getService().getMySavedTracks();
//            //                Log.d("MainActivity", String.valueOf(savedTrackPager.items.size()));
//            //                Parser(savedTrackPager.items);
//
//        } catch (RetrofitError error) {
//            SpotifyError spotifyError = SpotifyError.fromRetrofitError(error);
//            Log.d("MainActivity", spotifyError.toString());
//            // handle error
//        }

    }
    public String USER_ID = "";
    public void Parser (final List<SavedTrack> input) {
        Toast.makeText(getApplicationContext(), "Parser Called.", Toast.LENGTH_SHORT).show();
        final SpotifyApi spotify = new SpotifyApi();
        spotify.setAccessToken(ACCESS_TOKEN);
        spotify.getService().getMe(new Callback<UserPrivate>() {
            @Override
            public void success(UserPrivate userPrivate, Response response) {
                Parser2 (spotify, input);
                USER_ID = userPrivate.id;

            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("MainActivity", error.toString());
                Toast.makeText(getApplicationContext(), "Parser" + error.toString(), Toast.LENGTH_SHORT).show();
                return;

            }
        });
    }
    public String PLAYLIST_ID = "";

    public void Parser2 (final SpotifyApi spotify, final List<SavedTrack> input) {
        Toast.makeText(getApplicationContext(), "Parser2 Called", Toast.LENGTH_SHORT).show();
        spotify.getService().getMyPlaylists(new Callback<Pager<PlaylistSimple>>() {
            @Override
            public void success(Pager<PlaylistSimple> playlistSimplePager, Response response) {
                Toast.makeText(getApplicationContext(), "getMyPlaylists: Success!", Toast.LENGTH_SHORT).show();

                boolean doesExist = false;
                for (int i = 0; i < playlistSimplePager.items.size(); i++) {
                    if (playlistSimplePager.items.get(i).name.equals("Kairos Playlist")) {
                        PLAYLIST_ID = playlistSimplePager.items.get(i).id;
                        doesExist = true;
                        break;
                    }
                }
                Toast.makeText(getApplicationContext(), "Helper Finished.", Toast.LENGTH_SHORT).show();
                if (!doesExist) {
                    Map<String, Object> playlistOptions = new HashMap<>();
                    playlistOptions.put("name", "Kairos Playlist");
                    playlistOptions.put("public", true);
                    playlistOptions.put("description", ("A Playlist made with Kairos for:" + PLAYLIST_HRS + " hours and" + PLAYLIST_MINS + "minutes."));
                    spotify.getService().createPlaylist(USER_ID, playlistOptions, new SpotifyCallback<Playlist>() {
                        @Override
                        public void failure(SpotifyError spotifyError) {
                            Log.d("MainActivity", spotifyError.toString());
                            Toast.makeText(getApplicationContext(), "createPlaylist" + spotifyError.toString(), Toast.LENGTH_SHORT).show();
                            return;

                        }

                        @Override
                        public void success(Playlist playlist, Response response) {
                            Log.d("MainActivity", "Success");
                            Toast.makeText(getApplicationContext(), "createPlaylist: Success!", Toast.LENGTH_SHORT).show();
                            PLAYLIST_ID = playlist.id;
                            RecList(spotify, input);
                            return;
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "AlreadyMadePlaylist.", Toast.LENGTH_SHORT).show();
                    RecList(spotify, input);
                }

                return;
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("MainActivity", error.toString());
                Toast.makeText(getApplicationContext(), "getMyPlaylists" + error.toString(), Toast.LENGTH_SHORT).show();
                return;
            }
        });
    }

    public void RecList (final SpotifyApi spotify, List<SavedTrack> input) {
        Random r = new Random();
        String inputStringTracks = input.get(r.nextInt(input.size())).track.id;
        String inputStringArtist = input.get(r.nextInt(input.size())).track.artists.get(0).id;
        Log.d("MainActivity", "Track" + inputStringTracks);
        Log.d("MainActivity", "Artist" + inputStringArtist);


        Map<String, Object> options = new HashMap<>();

        options.put("limit", 100);
        options.put("seed_tracks", inputStringTracks);
        options.put("seed_artists", inputStringArtist);


        spotify.getService().getRecommendations(options, new Callback<Recommendations>() {
            @Override
            public void success(Recommendations recommendations, Response response) {
                Toast.makeText(getApplicationContext(), "getRecommendations: Success!", Toast.LENGTH_SHORT).show();
                Log.d("MainActivity", String.valueOf(recommendations.tracks.size()));
                addToPlaylist(recommendations, spotify);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("MainActivity", error.toString());
                Toast.makeText(getApplicationContext(), "getRecommendations" + error.toString() , Toast.LENGTH_SHORT).show();
                return;

            }
        });

    }
    @TargetApi(24)
    public void addToPlaylist (Recommendations input, SpotifyApi spotify) {
        Toast.makeText(getApplicationContext(), "addToPlaylist Called", Toast.LENGTH_SHORT).show();
        input.tracks.sort(new Comparator<Track>() {
            @Override
            public int compare(Track o1, Track o2) {
                if (o1.duration_ms > o2.duration_ms) {
                    return 1;
                }
                if (o1.duration_ms < o2.duration_ms) {
                    return -1;
                }
                return 0;
            }
        });
        String returnString = "";
        long PlaylistLength = PLAYLIST_DURATION;
        int i = 0;
        do {
            returnString += input.tracks.get(i).uri + ",";
            PlaylistLength = PlaylistLength - input.tracks.get(i).duration_ms;
            i++;
            if (i == input.tracks.size() - 1) {
                i = 0;
            }
        } while (PlaylistLength >= 0);


        spotify.getService().replaceTracksInPlaylist(USER_ID, PLAYLIST_ID, returnString, new Object(), new Callback<Result>() {
            @Override
            public void success(Result result, Response response) {
                Toast.makeText(getApplicationContext(), "replaceTracksInPlaylist: Success!" + result.toString(), Toast.LENGTH_SHORT).show();

                Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.spotify.mobile.android.ui");
                if (launchIntent != null) {
                    startActivity(launchIntent);//null pointer check in case package name was not found
                } else {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://open.spotify.com/user/" + USER_ID + "/playlist/" + PLAYLIST_ID));
                    startActivity(browserIntent);
                }
                return;
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("MainActivity", error.toString());
                Toast.makeText(getApplicationContext(), "replaceTracksInPlaylist" + error.toString(), Toast.LENGTH_SHORT).show();
                return;

            }
        });
    }

}
