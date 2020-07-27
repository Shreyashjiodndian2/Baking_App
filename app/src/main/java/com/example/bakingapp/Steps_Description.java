package com.example.bakingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.media.session.MediaButtonReceiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.session.MediaSession;
import android.media.session.PlaybackState;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import static com.example.bakingapp.StepsAdapter.steps;

public class Steps_Description extends AppCompatActivity implements View.OnClickListener, ExoPlayer.EventListener {
    private static final String TAG = "DESCRIPTION_ACTIVITY";
    private SimpleExoPlayer mExoPlayer;
    private PlayerView mPlayerView;
    private TextView mDescriptionTv;
    private Button next_Button;
    private Button prev_Button;
    private int mLength;
    private PlaybackState.Builder mStateBuilder;
    public static MediaSession mMediaSession;
    private NotificationManager mNotificationManager;
    public Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps_description);
        final Intent intent = getIntent();
        final Step step = intent.getParcelableExtra("step");
        mLength = intent.getIntExtra("length", -1);
        setTitle(intent.getStringExtra("name"));
        mPlayerView = findViewById(R.id.video_player);
        mPlayerView.setPlayer(mExoPlayer);
        mContext = this;
        ImageView error_Iv = (ImageView) findViewById(R.id.error_Iv);
        mPlayerView.setDefaultArtwork(BitmapFactory.decodeResource(getResources(), R.drawable.video_loading));
        String videoUrl = step.getVideoURL();
        if (videoUrl == null || videoUrl.length() == 0){
            mPlayerView.setVisibility(View.GONE);
            error_Iv.setVisibility(View.VISIBLE);
        }
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mDescriptionTv = findViewById(R.id.description_Tv);
        prev_Button = findViewById(R.id.prev_button);
        initalizeMediaSession();
        if (step.getId() != 0) {
            prev_Button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent1 = new Intent(getApplicationContext(), Steps_Description.class);
                    intent1.putExtra("step", (Parcelable) steps.get(step.getId()-1));
                    intent1.putExtra("name", intent.getStringExtra("name"));
                    intent1.putExtra("length", steps.size());
                    mExoPlayer.stop();
                    mExoPlayer.release();
                    startActivity(intent1);
                }
            });
        }
        next_Button = findViewById(R.id.next_Button);
        if (step.getId() != mLength-1){
            next_Button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent1 = new Intent(getApplicationContext(), Steps_Description.class);
                    intent1.putExtra("step", (Parcelable) steps.get(step.getId()+1));
                    intent1.putExtra("name", intent.getStringExtra("name"));
                    intent1.putExtra("length", steps.size());
                    mExoPlayer.stop();
                    mExoPlayer.release();

                    startActivity(intent1);
                }
            });
        }
        if (step.getId() == 0){
            prev_Button.setVisibility(View.GONE);
        } else if (step.getId() == mLength-1){
            next_Button.setVisibility(View.GONE);
        }

        if (videoUrl == null || videoUrl.length() == 0){
            mPlayerView.setDefaultArtwork(BitmapFactory.decodeResource(getResources(), R.drawable.youtube_blocked));
        }
        initializePlayer(Uri.parse(step.getVideoURL()));
        mDescriptionTv.setText(step.getDescription());
    }
    private void initalizeMediaSession(){
        mMediaSession = new MediaSession(this, TAG);
        mMediaSession.setFlags(MediaSession.FLAG_HANDLES_MEDIA_BUTTONS |
                MediaSession.FLAG_HANDLES_TRANSPORT_CONTROLS);
        mStateBuilder = new PlaybackState.Builder()
        .setActions(PlaybackState.ACTION_PAUSE | PlaybackState.ACTION_PLAY_PAUSE
        | PlaybackState.ACTION_SKIP_TO_NEXT
        | PlaybackState.ACTION_SKIP_TO_PREVIOUS);
        mMediaSession.setPlaybackState(mStateBuilder.build());
        mMediaSession.setCallback(new MySessionCallback());
        mMediaSession.setActive(true);
    }

    /**
     * Shows Media Style notification, with an action that depends on the current MediaSession
     * PlaybackState.
     * @param state The PlaybackState of the MediaSession.
     */
    private void showNotification(PlaybackState state) {
        Notification.Builder builder = new Notification.Builder(this);

        int icon;
        String play_pause;
        if(state.getState() == PlaybackState.STATE_PLAYING){
            icon = R.drawable.exo_controls_pause;
            play_pause = getString(R.string.pause);
        } else {
            icon = R.drawable.exo_controls_play;
            play_pause = getString(R.string.play);
        }


        Notification.Action playPauseAction = new Notification.Action(
                icon, play_pause,
                MediaButtonReceiver.buildMediaButtonPendingIntent(this,
                        PlaybackStateCompat.ACTION_PLAY_PAUSE));

        Notification.Action restartAction = new Notification.Action(R.drawable.exo_controls_previous, getString(R.string.restart),
                MediaButtonReceiver.buildMediaButtonPendingIntent
                        (this, PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS));

        PendingIntent contentPendingIntent = PendingIntent.getActivity
                (this, 0, new Intent(this, Steps_Description.class), 0);

        builder.setContentTitle(getString(R.string.guess))
                .setContentText(getString(R.string.notification_text))
                .setContentIntent(contentPendingIntent)
                .setSmallIcon(R.drawable.placeholder_1)
                .setVisibility(Notification.VISIBILITY_PUBLIC)
                .addAction(restartAction)
                .addAction(playPauseAction)
                .setStyle(new Notification.MediaStyle()
                        .setMediaSession(mMediaSession.getSessionToken()))
                        .build();


        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotificationManager.notify(0, builder.build());
    }

    public static class BroadCastReceiver extends BroadcastReceiver {
        public BroadCastReceiver() {        }
        @Override
        public void onReceive(Context context, Intent intent) {
            MediaButtonReceiver.handleIntent(MediaSessionCompat.fromMediaSession(context, mMediaSession), intent);
        }
    }
    private MediaSource buildMediaSource(Uri uri) {
        DataSource.Factory dataSourceFactory =
                new DefaultDataSourceFactory(this, "exoplayer-codelab");
        return new ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(uri);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            this.finish();
        }
        return true;
    }

    /**
     * Initialize ExoPlayer.
     * @param mediaUri The URI of the sample to play.
     */
    private void initializePlayer(Uri mediaUri) {
        Log.v("uri", mediaUri.toString());
        if (mExoPlayer == null) {
            // Create an instance of the ExoPlayer.
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(this, trackSelector, loadControl);
            mPlayerView.setPlayer(mExoPlayer);

            // Set the ExoPlayer.EventListener to this activity.
            mExoPlayer.addListener(this);

            // Prepare the MediaSource.
            String userAgent = Util.getUserAgent(this, getApplicationInfo().name);
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    this, userAgent), new DefaultExtractorsFactory(), null, null);
            mExoPlayer.prepare(buildMediaSource(mediaUri), false, false);
            mExoPlayer.setPlayWhenReady(true);
        }
    }
    @Override
    public void onClick(View view) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if((playbackState == ExoPlayer.STATE_READY) && playWhenReady){
            mStateBuilder.setState(PlaybackState.STATE_PLAYING,
                    mExoPlayer.getCurrentPosition(), 1f);
        } else if((playbackState == ExoPlayer.STATE_READY)){
            mStateBuilder.setState(PlaybackState.STATE_PAUSED,
                    mExoPlayer.getCurrentPosition(), 1f);
        }
        mMediaSession.setPlaybackState(mStateBuilder.build());
        showNotification(mStateBuilder.build());
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
        Log.v("Exoplayerexception", error.getMessage());
    }


    private class MySessionCallback extends MediaSession.Callback {
        @Override
        public void onPlay() {
            mExoPlayer.setPlayWhenReady(true);
        }

        @Override
        public void onPause() {
            mExoPlayer.setPlayWhenReady(false);
        }

        @Override
        public void onSkipToPrevious() {
            mExoPlayer.seekTo(0);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mExoPlayer.stop();
        mExoPlayer.release();
    }
}