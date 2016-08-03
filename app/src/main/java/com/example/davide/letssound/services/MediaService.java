package com.example.davide.letssound.services;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcelable;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.davide.letssound.helpers.NotificationHelper;

import java.io.FileDescriptor;
import java.io.IOException;
import java.lang.ref.WeakReference;

/**
 * Created by davide on 13/07/16.
 */
public class MediaService extends Service implements MediaPlayer.OnBufferingUpdateListener,
        MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener {
    private static final String TAG = "MediaService";
    public static final String PARAM_TRACK_TITLE = "TITLE";
    public static String PARAM_TRACK_THUMBNAIL = "PARAM_TRACK_THUMBNAIL";
    private MediaSessionCompat mediaSession;

    public static final String SESSION_TOKEN_TAG = "ls-token";
    public static String PARAM_TRACK_URI = "PARAM_TRACK_URI";
    private PlaybackStateCompat playbackState;
    private AudioManager audioManager;
    private MediaPlayer mediaPlayer;
    private MediaControllerCompat mediaController;
    private NotificationHelper notificationHelper;
    public int SEEKTO_MIN = 10000;
    private Uri mediaArtUri;
    private String soundTrackTitle;
    private Uri soundTrackUrl;



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
        public MediaSessionCompat.Token getMediaSessionToken() {
            return mediaSession.getSessionToken();
        }

    }
    
    @Override
    public void onCreate() {
        super.onCreate();
        try {
            initMediaSession();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        initNotification();

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
    private void initMediaSession() throws RemoteException {
        playbackState = new PlaybackStateCompat.Builder()
                .setState(PlaybackStateCompat.STATE_PLAYING, 0, 1.0f)
                .build();

        //set up media session
        mediaSession = new MediaSessionCompat(this, SESSION_TOKEN_TAG);
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
        mediaController = new MediaControllerCompat(getApplicationContext(), mediaSession.getSessionToken());
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
                case NotificationHelper.ACTION_STOP:
                    mediaController.getTransportControls().stop();
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
        mediaController.getTransportControls().stop();
        notificationHelper.cancel();
        //send broadcast event
//        sendBroadcast(new Intent());
    }
    @Override
    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
        Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        Log.e("hey", "on prepared");
        mediaPlayer.start();
        playbackState = new PlaybackStateCompat.Builder()
                .setState(PlaybackStateCompat.STATE_PLAYING, 0, 1.0f)
                .build();
        mediaSession.setPlaybackState(playbackState);
        notificationHelper
                .updateNotification(new WeakReference<>(playbackState),
                        soundTrackTitle, soundTrackUrl, mediaArtUri);
    }

    /**
     * @deprecated
     * @param extras
     * @return
     */
    private void initSoundTrackFromBundle(Bundle extras) {
        soundTrackUrl = extras.getParcelable(PARAM_TRACK_URI);
        mediaArtUri = extras.getParcelable(PARAM_TRACK_THUMBNAIL);
        soundTrackTitle = extras.getString(PARAM_TRACK_TITLE);
    }


    /**
     *
     */
    public MediaSessionCompat.Callback msCallback = new MediaSessionCompat.Callback() {

        @Override
        public void onPlay() {
            if (playbackState.getState() == PlaybackStateCompat.STATE_PAUSED) {
                mediaPlayer.start();

                playbackState = new PlaybackStateCompat.Builder()
                        .setState(PlaybackStateCompat.STATE_PLAYING, 0, 1.0f)
                        .build();
                mediaSession.setPlaybackState(playbackState);
                notificationHelper.updateNotification(new WeakReference<>(playbackState),
                        soundTrackTitle, soundTrackUrl, mediaArtUri);
            }
        }

        @Override
        public void onPlayFromSearch(final String query, final Bundle extras) {
            Log.e(TAG, "playing" + playbackState.getState());
            try {
//                FileDescriptor fd = getFileDescriptorFromBundle(extras);
//                mediaPlayer.setDataSource(fd);
                initSoundTrackFromBundle(extras);
                Log.e(TAG, soundTrackUrl.toString());
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mediaPlayer.reset();
                mediaPlayer.setDataSource(MediaService.this, soundTrackUrl);
                mediaPlayer.prepareAsync();

                PlaybackStateCompat playbackState = new PlaybackStateCompat.Builder()
                        .setState(PlaybackStateCompat.STATE_PLAYING, 0, 1.0f)
                        .build();
                mediaSession.setPlaybackState(playbackState);
                mediaSession.setMetadata(new MediaMetadataCompat.Builder()
                        .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, "ARTIST")
                        .putString(MediaMetadataCompat.METADATA_KEY_AUTHOR, "AUTHOR")
                        .putString(MediaMetadataCompat.METADATA_KEY_ALBUM, "ALBUM")
                        .putString(MediaMetadataCompat.METADATA_KEY_TITLE, "this is title")
                        .putString(MediaMetadataCompat.METADATA_KEY_ART_URI, mediaArtUri.toString())
                        .build());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onRewind() {
            if (playbackState.getState() == PlaybackStateCompat.STATE_PLAYING) {
                mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() - SEEKTO_MIN);
            }
        }

        @Override
        public void onFastForward() {
            if (playbackState.getState() == PlaybackStateCompat.STATE_PLAYING) {
                mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() - SEEKTO_MIN);
            }
        }

        @Override
        public void onPause() {
            if (playbackState.getState() == PlaybackStateCompat.STATE_PLAYING) {
                mediaPlayer.pause();
                playbackState = new PlaybackStateCompat.Builder()
                        .setState(PlaybackStateCompat.STATE_PAUSED, 0, 1.0f)
                        .build();
                mediaSession.setPlaybackState(playbackState);
                notificationHelper.updateNotification(new WeakReference<>(playbackState),
                        soundTrackTitle, soundTrackUrl, mediaArtUri);
            }
        }

        @Override
        public void onStop() {
            if (playbackState.getState() == PlaybackStateCompat.STATE_PLAYING) {
                mediaPlayer.stop();
                playbackState = new PlaybackStateCompat.Builder()
                        .setState(PlaybackStateCompat.STATE_STOPPED, 0, 1.0f)
                        .build();
                mediaSession.setPlaybackState(playbackState);
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
    };

}
