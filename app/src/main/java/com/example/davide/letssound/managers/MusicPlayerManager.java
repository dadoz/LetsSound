package com.example.davide.letssound.managers;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.example.davide.letssound.helpers.ObservableHelper;
import com.example.davide.letssound.services.MediaService;
import com.example.davide.letssound.utils.Utils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import rx.Observable;
import rx.functions.Func1;

//TODO merge with controller one
/**
 * Created by davide on 15/07/16.
 */
public class MusicPlayerManager implements ObservableHelper.ObservableHelperInterface {
    private static final String TAG = "MusicPlayerManagerTAG";
    private static MusicPlayerManager instance;
    private static RetrofitYoutubeDownloaderManager retrofitManager;
    private static ObservableHelper observableHelper;
    private static WeakReference<OnMusicPlayerCallback> listener;
    private String thumbnailUrl;
    private String title;


    /**
     *
     * @param lst
     * @return
     */
    public static MusicPlayerManager getInstance(WeakReference<OnMusicPlayerCallback> lst) {
        if (instance == null) {
            instance = new MusicPlayerManager();
        }
        retrofitManager = RetrofitYoutubeDownloaderManager.getInstance();
        listener = lst;
        observableHelper = new ObservableHelper(new WeakReference<ObservableHelper.ObservableHelperInterface>(instance));
        return instance;
    }


    /**
     *
     * @param videoId
     */
    public void playSoundTrack(String videoId, String thumbnailUrl, String title) {
        this.thumbnailUrl = thumbnailUrl;
        this.title = title;
        fetchSoundTrackUrl(videoId);
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
        playMedia((String) obj);
    }


    @Override
    public void onObservableEmpty() {

    }

    @Override
    public void onObservableError(String type, String error) {
        Log.e(TAG, "error" + error);
    }

    /**
     * play media by controller
     * @param videoUrl
     */
    private void playMedia(String videoUrl) {
        //TODO pay attention on this
        Bundle bundle = Utils.buildPlayBundle(videoUrl, thumbnailUrl, title);
        listener.get().onPlayMediaCallback(bundle);
    }


    public interface OnMusicPlayerCallback {
        void onPlayMediaCallback(Bundle bundle);
    }

}
