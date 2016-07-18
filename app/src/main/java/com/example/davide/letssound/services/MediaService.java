package com.example.davide.letssound.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.drm.DrmStore;
import android.media.MediaMetadata;
import android.media.MediaRouter;
import android.media.browse.MediaBrowser;
import android.media.session.MediaController;
import android.media.session.MediaSession;
import android.media.session.PlaybackState;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.service.media.MediaBrowserService;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.davide.letssound.MainActivity;
import com.example.davide.letssound.R;
import com.example.davide.letssound.helpers.LocalPlayback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by davide on 13/07/16.
 */
public class MediaService extends Service {
    private NotificationManager nm;
    private final static int NOTIFICATION_ID = 999999;
    private IBinder binder = new MediaBinder();
    private LocalPlayback localPlayback;
    private MediaSession mediaSession;
    private int REQUEST_CODE = 99;

    /**
     *
     */
    public class MediaBinder extends Binder {
        public MediaService getService() {
            return MediaService.this;
        }
    }
    
    @Override
    public void onCreate() {
        super.onCreate();
        initNotification();
        showNotification();
        initMediaSession();
        Log.e("TAG", "hey service start");
    }

    /**
     * init mediasession
     */
    private void initMediaSession() {
//        musicLightCallbackHandlers = new MusicLightCallbackHandlers(this);
//        mPlayingQueue = new ArrayList<>();
//        mMusicProvider = new MusicProvider();
//        mPackageValidator = new PackageValidator(this);

        // Start a new MediaSession
        MediaSessionCallback mediaSessionCallback = new MediaSessionCallback();
        mediaSession = new MediaSession(this, "MusicService");
        mediaSession.setCallback(mediaSessionCallback);
        mediaSession.setFlags(MediaSession.FLAG_HANDLES_MEDIA_BUTTONS |
                MediaSession.FLAG_HANDLES_TRANSPORT_CONTROLS);

        localPlayback = new LocalPlayback(this);
        localPlayback.setCallback(mediaSessionCallback);
//        localPlayback.setState(PlaybackState.STATE_NONE);
//        localPlayback.start();

        PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), REQUEST_CODE,
                new Intent(getApplicationContext(), MainActivity.class),
                PendingIntent.FLAG_UPDATE_CURRENT);
        mediaSession.setSessionActivity(pi);
//        musicLightCallbackHandlers.updatePlaybackState(null);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)  {
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        nm.cancel(NOTIFICATION_ID);
    }

    /**
     *
     */
    public class MediaSessionCallback extends MediaSession.Callback {
        @Override
        public void onPlay() {
        }

        @Override
        public void onSkipToQueueItem(long queueId) {
        }

        @Override
        public void onSeekTo(long position) {
        }

        @Override
        public void onPlayFromMediaId(String mediaId, Bundle extras) {
        }

        @Override
        public void onPause() {
        }

        @Override
        public void onStop() {
        }

        @Override
        public void onSkipToNext() {

        }

        @Override
        public void onSkipToPrevious() {

        }

        @Override
        public void onCustomAction(@NonNull String action, Bundle extras) {

        }

        @Override
        public void onPlayFromSearch(final String query, final Bundle extras) {
        }
    }

    public void prepareMediaSessionToPlay() {
        //fill queue to playable songs
//        QueueHelper.getPlayingQueue(mediaId, mMusicProvider);
        MediaSession.QueueItem rmp = new MediaSession.QueueItem();
        mediaSession.setQueue();
    }

    /**
     * 
     */
    private void initNotification() {
        nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    /**
     *
     */
    private void showNotification() {
        String text = "hey service";

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0);
        Notification notification = new Notification.Builder(this)
                .setSmallIcon(R.drawable.play_icon)
                .setTicker(text)
                .setWhen(System.currentTimeMillis())
                .setContentTitle("hey new content")
                .setContentText(text)
                .setContentIntent(contentIntent)
                .build();
        nm.notify(NOTIFICATION_ID, notification);
    }

//    public class MediaPlayerController {
//        private final MediaSession mediaSession;
//
//        public MediaPlayerController(MediaSession mediaSession) {
//            this.mediaSession = mediaSession;
//        }
//        private void playMedia() {
//            mediaSession.getController().getTransportControls().play();
//        }
//
//        private void pauseMedia() {
//            mediaSession.getController().getTransportControls().pause();
//            }
//        }
//
//    }
}
