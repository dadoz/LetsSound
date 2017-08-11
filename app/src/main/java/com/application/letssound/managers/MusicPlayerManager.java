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
import com.application.letssound.helpers.ObservableHelper;
import com.application.letssound.utils.Utils;
import com.lib.lmn.davide.soundtrackdownloaderlibrary.manager.SoundTrackDownloaderManager;
import com.lib.lmn.davide.soundtrackdownloaderlibrary.modules.SoundTrackDownloaderModule;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import rx.Observable;
import rx.functions.Func1;

public class MusicPlayerManager implements ObservableHelper.ObservableHelperInterface {
    private static final String TAG = "MusicPlayerManagerTAG";
    private static MusicPlayerManager instance; //FIXME LEAK
    private RetrofitYoutubeDownloaderManager retrofitManager;
    private ObservableHelper observableHelper;
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
        observableHelper = new ObservableHelper(instance);
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
        Observable<Object> obs = retrofitManager.fetchUrlByVideoId(videoId)
                .map(new Func1<String, Object>() {
            @Override
            public Object call(String s) {
                return s;
            }
        });
        observableHelper.initObservableObject(obs);
    }

    @Override
    public void onObservableSuccess(ArrayList<Object> list, String requestType) {
    }

    @Override
    public void onObservableSuccess(Object obj, String requestType) {
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
    public void onObservableEmpty() {

    }

    @Override
    public void onObservableError(String type, String error) {
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
