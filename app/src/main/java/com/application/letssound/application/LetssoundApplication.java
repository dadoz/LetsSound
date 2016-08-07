package com.application.letssound.application;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.media.session.MediaControllerCompat;

import com.application.letssound.R;
import com.application.letssound.services.MediaService;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class LetssoundApplication extends Application {
    private static final String TAG = "LetssoundApplication";
    private static final String FONT_PATH = "fonts/KozGoPro-Light.ttf";
    private MediaService serviceRef;

    @Override
    public void onCreate() {
        super.onCreate();
        initCalligraph();
    }

    /**
     *
     */
    private void initCalligraph() {
        CalligraphyConfig.initDefault(new CalligraphyConfig
                .Builder()
                .setDefaultFontPath(FONT_PATH)
                .setFontAttrId(R.attr.fontPath)
                .build());
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
                serviceRef = ((MediaService.MediaBinder) iBinder).getService();
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
    /**
     *
     * @return
     */
    public MediaService getMediaService() {
        return serviceRef;
    }
}
