package com.application.letssound.managers;

import android.util.Log;

import com.application.letssound.helpers.ObservableHelper;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import rx.Subscription;

public class MediaSearchManager implements MediaSearchManagerInterface,
        ObservableHelper.ObservableHelperInterface {
    private static final String TAG = "MediaSearchManager";
    private static MediaSearchManager instance; //FIXME LEAK
    private YoutubeV3RetrofitManager retrofitManager;
    private ObservableHelper observableHelper;
    private WeakReference<TrackSearchManagerInterface> listener;

    /**
     *
     * @param lst
     */
    public MediaSearchManager(WeakReference<TrackSearchManagerInterface> lst) {
        listener = lst;
        retrofitManager = YoutubeV3RetrofitManager.getInstance();
        observableHelper = new ObservableHelper(this);
    }

    /**
     *
     * @return
     */
    public static MediaSearchManager getInstance(WeakReference<TrackSearchManagerInterface> lst) {
        if (instance == null) {
            instance = new MediaSearchManager(lst);
        }
        return instance;
    }

    @Override
    public Object onSearchSync(String query) {
        return null;
    }

    @Override
    public Subscription onSearchAsync(String query) {
        return observableHelper.initObservable(retrofitManager.searchList(query));
    }

    @Override
    public void onObservableSuccess(ArrayList<Object> list, String requestType) {
        Log.e(TAG, "success results  " + list.size());
        listener.get().onTrackSearchSuccess(list);
    }

    @Override
    public void onObservableSuccess(Object obj, String requestType) {
    }

    @Override
    public void onObservableEmpty() {
        Log.e(TAG, "empty");
        listener.get().onTrackSearchError("empty fields");
    }

    @Override
    public void onObservableError(String requestType, String error) {
        Log.e(TAG, "hey" + error);
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
