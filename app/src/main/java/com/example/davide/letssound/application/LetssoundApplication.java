package com.example.davide.letssound.application;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.session.MediaController;
import android.media.session.PlaybackState;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;

import com.example.davide.letssound.services.MediaService;

import java.util.List;

/**
 * Created by davide on 01/08/16.
 */
public class LetssoundApplication extends Application {
    private static final String TAG = "LetssoundApplication";

    @Override
    public void onCreate() {
        super.onCreate();
        initCalligraph();
    }

    /**
     *
     */
    private void initCalligraph() {
//        CalligraphyConfig.initDefault(new CalligraphyConfig
//                .Builder()
//                .setDefaultFontPath(FONT_PATH)
//                .setFontAttrId(R.attr.fontPath)
//                .build());
    }

    /**
     * start service and bind it
     */
    public void doBindService() {
        bindService(new Intent(this, MediaService.class),
                serviceConnection, Context.BIND_AUTO_CREATE);
    }

    /**
     * undbind service
     */
    public void doUnbindService() {
        unbindService(serviceConnection);
    }
    public MediaControllerCompat mediaController;

    /**
     *
     */
    public ServiceConnection serviceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            try {
                mediaController = new MediaControllerCompat(LetssoundApplication.this,
                        ((MediaService.MediaBinder) iBinder).getMediaSessionToken());
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mediaController = null;
        }
    };

    /**
     * @deprecated
     */
    public void playMedia(Bundle bundle) {
        mediaController.getTransportControls().playFromSearch("", bundle);
    }

    /**
     *
     * @return
     */
    public MediaControllerCompat getMediaControllerInstance() {
        return mediaController;
    }
}
