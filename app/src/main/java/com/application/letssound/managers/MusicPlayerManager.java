package com.application.letssound.managers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.session.PlaybackState;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.view.View;

import com.application.letssound.SoundTrackPlayerActivity;
import com.application.letssound.application.LetssoundApplication;
import com.application.letssound.helpers.SoundTrackRetrofitResponseCallbacks;
import com.application.letssound.utils.Utils;
import com.lib.lmn.davide.soundtrackdownloaderlibrary.manager.SoundTrackDownloaderManager;
import com.lib.lmn.davide.soundtrackdownloaderlibrary.modules.SoundTrackDownloaderModule;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MusicPlayerManager implements SoundTrackRetrofitResponseCallbacks {
    private static final String TAG = "MusicPlayerManagerTAG";
    private static MusicPlayerManager instance; //FIXME LEAK
    private RetrofitYoutubeDownloaderManager retrofitManager;
    private WeakReference<OnMusicPlayerCallback> listener;
    private WeakReference<Activity> activityRef;
    private View playButtonView;
    private View pauseButtonView;
    private MediaControllerCompat mediaControllerRef; //FIXME LEAK
    private String thumbnailUrl;
    private String title;

    public MusicPlayerManager(Activity activity, View[] viewsArray,
                              OnMusicPlayerCallback lst) {
        retrofitManager = RetrofitYoutubeDownloaderManager.getInstance();
        listener = new WeakReference<OnMusicPlayerCallback>(lst);
        activityRef = new WeakReference<Activity>(activity);
        if (viewsArray != null) {
            playButtonView = viewsArray[0];
            pauseButtonView = viewsArray[1];
        }
        mediaControllerRef = ((LetssoundApplication) activityRef.get().getApplication()).getMediaControllerInstance();
    }

    /**
     *
     * @param lst
     * @return
     */
    public static MusicPlayerManager getInstance(@NonNull  Activity activity, View[] viewsArray,
                                                 OnMusicPlayerCallback lst) {
        if (instance == null) {
            instance = new MusicPlayerManager(activity, viewsArray, lst);
        }
        return instance;
    }


    /**
     *
     * @param videoId
     */
    public void playSoundTrack(Context context, SoundTrackDownloaderModule.OnSoundTrackRetrievesCallbacks lst,
                               String videoId, String thumbnailUrl, String title) {
        this.thumbnailUrl = thumbnailUrl;
        this.title = title;
        SoundTrackDownloaderManager.Companion.getInstance(context, lst).downloadAndPlaySoundTrack(videoId);

//        fetchSoundTrackUrl(videoId);
    }

    /**
     *
     * @param videoId
     */
    public void fetchSoundTrackUrl(String videoId) {
        Log.e(TAG, "fetch" + videoId);
        retrofitManager.fetchUrlByVideoId(videoId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onObservableSuccess,
                        (throwable) -> onObservableError(throwable.getMessage()));
    }

    @Override
    public void onObservableSuccess(ArrayList<Object> list) {
    }

    @Override
    public void onObservableSuccess(Object obj) {
        Log.e(TAG, "success music player " + obj.toString());
        handlePlayWithBundle((String) obj);
    }

    /**
     *
     * @param soundTrackUrl
     */
    private void handlePlayWithBundle(String soundTrackUrl) {
        Bundle bundle = Utils.buildPlayBundle(soundTrackUrl, thumbnailUrl, title);
        ((LetssoundApplication) activityRef.get().getApplication()).playMedia(bundle);
        //TODO handle callback (if error should not start activity
        Intent intent = new Intent(activityRef.get().getApplicationContext(), SoundTrackPlayerActivity.class);
        intent.putExtras(bundle);
        activityRef.get().startActivity(intent);
    }

    @Override
    public void onObservableError(String error) {
        Log.e(TAG, "error" + error);
    }

    public interface OnMusicPlayerCallback {
        void onPlayMediaCallback(Bundle bundle);
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
        if (playButtonView != null)
            playButtonView.setVisibility(toBePlayed ? View.VISIBLE : View.GONE);
        if (pauseButtonView != null)
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
     * @param bundle
     */
    public void repeatOne(Bundle bundle) {
        ((LetssoundApplication) activityRef.get().getApplication()).playMedia(bundle);
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
