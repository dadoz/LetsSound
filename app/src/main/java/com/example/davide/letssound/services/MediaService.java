package com.example.davide.letssound.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.drm.DrmStore;
import android.graphics.drawable.Icon;
import android.media.AudioManager;
import android.media.MediaDataSource;
import android.media.MediaMetadata;
import android.media.MediaPlayer;
import android.media.MediaRouter;
import android.media.browse.MediaBrowser;
import android.media.session.MediaController;
import android.media.session.MediaSession;
import android.media.session.PlaybackState;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.service.media.MediaBrowserService;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.example.davide.letssound.MainActivity;
import com.example.davide.letssound.R;
import com.example.davide.letssound.helpers.LocalPlayback;
import com.example.davide.letssound.helpers.MediaSessionQueueHelper;
import com.example.davide.letssound.helpers.NotificationHelper;

import java.io.FileDescriptor;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by davide on 13/07/16.
 */
public class MediaService extends Service implements MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener {
    private static final String TAG = "MediaService";
    private MediaSession mediaSession;

    public static final String SESSION_TOKEN_TAG = "ls-token";
    public static String PARAM_TRACK_URI = "PARAM_TRACK_URI";
    private PlaybackState playbackState;
    private AudioManager audioManager;
    private MediaPlayer mediaPlayer;
    private MediaController mediaController;
    private NotificationHelper notificationHelper;
    public int SEEKTO_MIN = 10000;


    /**
     *
     */
    public class MediaBinder extends Binder {
        /**
         *
         * @return
         */
        public MediaService getService() {
            return MediaService.this;
        }

        /**
         *
         * @return
         */
        public MediaSession.Token getMediaSessionToken() {
            return mediaSession.getSessionToken();
        }

    }
    
    @Override
    public void onCreate() {
        super.onCreate();
        initNotification();
        initMediaSession();
        getSystemService(NOTIFICATION_SERVICE);
        Log.e("TAG", "hey service start");
    }

    /**
     *
     */
    private void initNotification() {
        notificationHelper = new NotificationHelper(new WeakReference<>(this),
                new WeakReference<>(mediaSession), new WeakReference<>(playbackState));
    }

    /**
     * init mediasession
     */
    private void initMediaSession() {
        playbackState = new PlaybackState.Builder()
                .setState(PlaybackState.STATE_PLAYING, 0, 1.0f)
                .build();

        //set up media session
        mediaSession = new MediaSession(this, SESSION_TOKEN_TAG);
        mediaSession.setCallback(msCallback);
        mediaSession.setActive(true);
        mediaSession.setPlaybackState(playbackState);

        //set up audio manager
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);

        //create media player
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setOnBufferingUpdateListener(this);

        //create media controller
        mediaController = new MediaController(getApplicationContext(), mediaSession.getSessionToken());

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MediaBinder();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)  {
        if (intent != null &&
                intent.getAction() != null) {
            switch (intent.getAction()) {
                case NotificationHelper.ACTION_PLAY:
                    mediaController.getTransportControls().play();
                    break;
                case NotificationHelper.ACTION_FAST_FORWARD:
                    mediaController.getTransportControls().fastForward();
                    break;
                case NotificationHelper.ACTION_REWIND:
                    mediaController.getTransportControls().rewind();
                    break;
                case NotificationHelper.ACTION_PAUSE:
                    mediaController.getTransportControls().pause();
                    break;
            }
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        notificationHelper.cancel();
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        Log.e("hey", "on prepared");
        mediaPlayer.start();
        playbackState = new PlaybackState.Builder()
                .setState(PlaybackState.STATE_PLAYING, 0, 1.0f)
                .build();
        mediaSession.setPlaybackState(playbackState);
        notificationHelper.updateNotification();
    }

    /**
     * @deprecated
     * @param bundle
     * @return
     * @throws IOException
     */
    private Uri getUriFromBundle(Bundle bundle) {
        return bundle.getParcelable(PARAM_TRACK_URI);
    }

    /**
     * @deprecated
     * @param bundle
     * @return
     * @throws IOException
     */
    public FileDescriptor getFileDescriptorFromBundle(Bundle bundle) throws IOException {
        String fileName = bundle.getString(PARAM_TRACK_URI);
        return getApplicationContext().getAssets().openFd(fileName).getFileDescriptor();
    }

    /**
     *
     */
    public MediaSession.Callback msCallback = new MediaSession.Callback() {

        @Override
        public void onPlay() {
            if (playbackState.getState() == PlaybackState.STATE_PAUSED) {
                mediaPlayer.start();

                playbackState = new PlaybackState.Builder()
                        .setState(PlaybackState.STATE_PLAYING, 0, 1.0f)
                        .build();
                mediaSession.setPlaybackState(playbackState);
                notificationHelper.updateNotification();
            }
        }

        @Override
        public void onRewind() {
            if (playbackState.getState() == PlaybackState.STATE_PLAYING) {
                mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() - SEEKTO_MIN);
            }
        }

        @Override
        public void onFastForward() {
            if (playbackState.getState() == PlaybackState.STATE_PLAYING) {
                mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() - SEEKTO_MIN);
            }
        }

        @Override
        public void onPause() {
            if (playbackState.getState() == PlaybackState.STATE_PLAYING) {
                mediaPlayer.pause();
                playbackState = new PlaybackState.Builder()
                        .setState(PlaybackState.STATE_PAUSED, 0, 1.0f)
                        .build();
                mediaSession.setPlaybackState(playbackState);
                notificationHelper.updateNotification();
            }
        }

        @Override
        public void onStop() {
            if (playbackState.getState() == PlaybackState.STATE_PLAYING) {
                mediaPlayer.stop();
                playbackState = new PlaybackState.Builder()
                        .setState(PlaybackState.STATE_STOPPED, 0, 1.0f)
                        .build();
                mediaSession.setPlaybackState(playbackState);
                notificationHelper.updateNotification();
            }
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
            Log.e(TAG, "playing");
            try {
//                FileDescriptor fd = getFileDescriptorFromBundle(extras);
//                mediaPlayer.setDataSource(fd);
                Uri url = getUriFromBundle(extras);
                mediaPlayer.reset();
                mediaPlayer.setDataSource(MediaService.this, url);
                mediaPlayer.prepareAsync();

                PlaybackState playbackState = new PlaybackState.Builder()
                        .setState(PlaybackState.STATE_CONNECTING, 0, 1.0f)
                        .build();
                mediaSession.setPlaybackState(playbackState);
                mediaSession.setMetadata(new MediaMetadata.Builder()
                        .putString(MediaMetadata.METADATA_KEY_ARTIST, "ARTIST")
                        .putString(MediaMetadata.METADATA_KEY_AUTHOR, "AUTHOR")
                        .putString(MediaMetadata.METADATA_KEY_ALBUM, "ALBUM")
                        .putString(MediaMetadata.METADATA_KEY_TITLE, "this is title")
                        .build());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

}
