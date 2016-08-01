package com.example.davide.letssound.managers;

import android.app.Activity;
import android.media.session.PlaybackState;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.view.View;

import com.example.davide.letssound.application.LetssoundApplication;

import java.lang.ref.WeakReference;

/**
 * Created by davide on 01/08/16.
 */
public class MusicPlayerControllerManager {
    private static WeakReference<Activity> activityRef;
    private static MusicPlayerControllerManager instance;
    private static MediaControllerCompat mediaControllerRef;
    public String TAG = "MusicPlayerControllerManager";
    private static View playButtonView;
    private static View pauseButtonView;


    public static MusicPlayerControllerManager getInstance(WeakReference<Activity> activity, @NonNull View[] viewsArray) {
        activityRef = activity;
        playButtonView = viewsArray[0];
        pauseButtonView = viewsArray[1];
        //TODO weak ref??
        mediaControllerRef = ((LetssoundApplication) activityRef.get().getApplication()).getMediaControllerInstance();
        return instance == null ?
                instance = new MusicPlayerControllerManager() : instance;
    }

    /**
     *
     */
    public void initMediaController() {
        if (mediaControllerRef != null) {
            mediaControllerRef.registerCallback(mediaControllerCallback);
        }
    }

    /**
     *
     * @param toBePlayed
     */
    public void setPlayButton(boolean toBePlayed) {
        playButtonView.setVisibility(toBePlayed ? View.VISIBLE : View.GONE);
        pauseButtonView.setVisibility(toBePlayed ? View.GONE : View.VISIBLE);
    }

    /**
     *
     */
    public void play() {
        if (mediaControllerRef != null) {
            mediaControllerRef.getTransportControls().play();
        }

    }
    /**
     *
     */
    public void pause() {
        if (mediaControllerRef != null) {
            mediaControllerRef.getTransportControls().pause();
        }
    }
    /**
     *
     */
    public void fastForward() {
        if (mediaControllerRef != null) {
            mediaControllerRef.getTransportControls().play();
        }

    }
    /**
     *
     */
    public void rewind() {
        if (mediaControllerRef != null) {
            mediaControllerRef.getTransportControls().rewind();
        }

    }
    /**
     *
     */
    public void repeatOne() {
        if (mediaControllerRef != null) {
            mediaControllerRef.getTransportControls().seekTo(0);
        }
    }

    /**
     *
     */
    public MediaControllerCompat.Callback mediaControllerCallback = new MediaControllerCompat.Callback() {

        @Override
        public void onPlaybackStateChanged(PlaybackStateCompat state) {

            switch (state.getState()) {
                case PlaybackState.STATE_NONE:
                    break;
                case PlaybackState.STATE_PLAYING:
                    Log.e(TAG, "to be paused");
                    setPlayButton(false);
                    break;
                case PlaybackState.STATE_PAUSED:
                    setPlayButton(true);
                    Log.e(TAG, "to be played");
                    break;
                case PlaybackState.STATE_FAST_FORWARDING:
                    break;
                case PlaybackState.STATE_REWINDING:
                    break;
            }
        }

        @Override
        public void onMetadataChanged(MediaMetadataCompat metadata) {
            super.onMetadataChanged(metadata);
            //update title
        }

    };

}
