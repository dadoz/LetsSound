package com.example.davide.letssound.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.lang.ref.WeakReference;

/**
 * Created by davide on 23/07/16.
 */
public class ConnectivityUtils {

    /**
     *
     * @param contextWeakReference
     * @return
     */
    public static boolean isConnected(WeakReference<Context> contextWeakReference) {
        NetworkInfo networkInfo = ((ConnectivityManager) contextWeakReference.get()
                .getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        return networkInfo != null &&
                networkInfo.isConnectedOrConnecting();
    }

}
