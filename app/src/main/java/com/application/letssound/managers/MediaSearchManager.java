package com.application.letssound.managers;

import android.util.Log;


import com.application.letssound.helpers.SoundTrackRetrofitResponseCallbacks;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MediaSearchManager implements MediaSearchManagerInterface,
        SoundTrackRetrofitResponseCallbacks {
    private static final String TAG = "MediaSearchManager";
    private static MediaSearchManager instance; //FIXME LEAK
    private YoutubeV3RetrofitManager retrofitManager;
    private WeakReference<TrackSearchManagerInterface> listener;

    /**
     *
     * @param lst
     */
    public MediaSearchManager(TrackSearchManagerInterface lst) {
        listener = new WeakReference<>(lst);
        retrofitManager = YoutubeV3RetrofitManager.getInstance();
    }

    /**
     *
     * @return
     */
    public static MediaSearchManager getInstance(TrackSearchManagerInterface lst) {
        return new MediaSearchManager(lst);
        //FIXME when you have time -> not set listener on resume activity
//        if (instance == null) {
//            instance = new MediaSearchManager(lst);
//        }
//        return instance;
    }

    @Override
    public Object onSearchSync(String query) {
        return null;
    }

    @Override
    public Subscription onSearchAsync(String query) {
        return retrofitManager.searchList(query)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .filter(list -> !list.isEmpty())
                .subscribe(this::onObservableSuccess,
                        (throwable) -> onObservableError(throwable.getMessage()));
    }

    @Override
    public void onObservableSuccess(ArrayList<Object> list) {
        Log.e(TAG, "success results  " + list.size());
        if (listener.get() != null)
            listener.get().onTrackSearchSuccess(list);
    }

    @Override
    public void onObservableSuccess(Object obj) {
    }

//    @Override
//    public void onObservableEmpty() {
//        Log.e(TAG, "empty");
//        if (listener.get() != null)
//            listener.get().onTrackSearchError("empty fields");
//    }

    @Override
    public void onObservableError(String error) {
        Log.e(TAG, "hey" + error);
        if (listener.get() != null)
            listener.get().onTrackSearchError(error);
    }

    public interface TrackSearchManagerInterface {
        void onTrackSearchSuccess(Object list);
        void onTrackSearchError(String error);
    }

    public void destroy() {
        retrofitManager.destroy();
        instance = null;
    }
}
