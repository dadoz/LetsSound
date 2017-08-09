package com.application.letssound.helpers;

import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ObservableHelper {

    private static final String TAG = "ObservableHelperTag";
    private WeakReference<ObservableHelperInterface> listener;
    private String requestType;

    public ObservableHelper(ObservableHelperInterface lst) {
        listener = new WeakReference<ObservableHelperInterface>(lst);
    }

    /**
     *
     * @param observable
     */
    public Subscription initObservable(Observable<ArrayList<Object>> observable) {
        return observable
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ArrayList<Object>>() {

                    @Override
                    public void onCompleted() {
                        Log.e(TAG, "completed");
                    }

                    @Override
                    public void onError(Throwable e) {
                        setRequestType();
                        if (listener.get() != null) {
                            listener.get().onObservableError(requestType, e.getMessage());
                        }
                    }

                    @Override
                    public void onNext(ArrayList<Object> list) {
                        setRequestType();
                        if ((list == null || list.size() == 0) &&
                                listener.get() != null) {
                            listener.get().onObservableEmpty();
                            return;
                        }

                        if (listener.get() != null) {

                            listener.get().onObservableSuccess(list, requestType);
                        }
                    }
                });
    }

    /**
     *
     * @param observable
     */
    public Subscription initObservableObject(Observable<Object> observable) {
        return observable
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Object>() {

                    @Override
                    public void onCompleted() {
                        Log.e(TAG, "completed");
                    }

                    @Override
                    public void onError(Throwable e) {
                        setRequestType();
                        if (listener.get() != null) {
                            listener.get().onObservableError(requestType, e.getMessage());
                        }
                    }

                    @Override
                    public void onNext(Object obj) {
                        setRequestType();
                        if (obj == null &&
                                listener.get() != null) {
                            listener.get().onObservableEmpty();
                            return;
                        }

                        if (listener.get() != null) {
                            listener.get().onObservableSuccess(obj, requestType);
                        }
                    }
                });
    }

    /**
     *
     */
    private void setRequestType() {
        requestType = " - ";
        //requestType = updateRequestType(list.get(0)); //TODO FIX

    }

    /**
     *
     */
    public interface ObservableHelperInterface {
        void onObservableSuccess(ArrayList<Object> list, String requestType);
        void onObservableSuccess(Object obj, String requestType);
        void onObservableEmpty();
        void onObservableError(String type, String requestType);
    }
}