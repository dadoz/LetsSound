package com.example.davide.letssound.managers;

import android.util.Log;

import com.example.davide.letssound.helpers.ObservableHelper;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import rx.Subscription;

public class MediaSearchManager implements MediaSearchManagerInterface,
        ObservableHelper.ObservableHelperInterface {
    private static final String TAG = "MediaSearchManager";
    private static MediaSearchManager instance;
    private static RetrofitManager retrofitManager;
    private static ObservableHelper observableHelper;
    private static WeakReference<TrackSearchManagerInterface> listener;

    /**
     *
     * @return
     */
    public static MediaSearchManager getInstance(WeakReference<TrackSearchManagerInterface> lst) {
        if (instance == null) {
            instance = new MediaSearchManager();
        }
        listener = lst;
        retrofitManager = RetrofitManager.getInstance();
        observableHelper = new ObservableHelper(new WeakReference<ObservableHelper.ObservableHelperInterface>(instance));
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

}
