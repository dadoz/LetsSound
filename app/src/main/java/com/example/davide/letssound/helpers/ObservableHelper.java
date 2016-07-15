package com.example.davide.letssound.helpers;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by davide on 15/07/16.
 */
public class ObservableHelper {

    private static ObservableHelper instance;
    private static WeakReference<ObservableHelperInterface> listener;
    private String requestType;

    public static ObservableHelper getInstance(WeakReference<ObservableHelperInterface> lst) {
        listener = lst;
        return instance == null ?
                instance = new ObservableHelper() : instance;
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
                    }

                    @Override
                    public void onError(Throwable e) {
                        setRequestType();
                        if (listener.get() != null) {
                            listener.get().onObservableError(requestType);
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
     */
    private void setRequestType() {
        requestType = "";
        //requestType = updateRequestType(list.get(0)); //TODO FIX

    }

    /**
     *
     */
    private interface ObservableHelperInterface {
        void onObservableSuccess(ArrayList<Object> list, String requestType);
        void onObservableEmpty();
        void onObservableError(String requestType);
    }
}
