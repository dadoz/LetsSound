package com.example.davide.letssound.helpers;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.session.PlaybackState;
import android.net.Uri;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.android.volley.VolleyError;
import com.example.davide.letssound.MainActivity;
import com.example.davide.letssound.R;
import com.example.davide.letssound.managers.VolleyMediaArtManager;
import com.example.davide.letssound.services.MediaService;

import java.lang.ref.WeakReference;

public class NotificationHelper implements VolleyMediaArtManager.OnVolleyMediaArtCallbackInterface {
    private final static int NOTIFICATION_ID = 999999;

    public static final String ACTION_PLAY = "play";
    public static final String ACTION_PAUSE = "pause";
    public static final String ACTION_FAST_FORWARD = "fastForward";
    public static final String ACTION_REWIND = "rewind";
    public static final String ACTION_STOP = "stop";
    private static final String TAG = "NotificationHelper";

    private static WeakReference<MediaService> serviceRef;
    private static WeakReference<PlaybackStateCompat> playbackStateRef;
    private static WeakReference<MediaSessionCompat> mediaSessionRef;
    private NotificationManagerCompat nm;
    private NotificationCompat.Builder notificationBuilder;

    public NotificationHelper(WeakReference<MediaService> srv, WeakReference<MediaSessionCompat> ms,
                              WeakReference<PlaybackStateCompat> ps) {
        serviceRef = srv;
        mediaSessionRef = ms;
        playbackStateRef = ps; //TODO leak
        nm = NotificationManagerCompat.from(serviceRef.get());
    }

    /**
     *
     * @param playbackState
     * @param mediaArtUri
     */
    public void updateNotification(WeakReference<PlaybackStateCompat> playbackState, Uri mediaArtUri) {
        retrieveMediaArtAsync(mediaArtUri);
        updatePlaybackCallbackState(playbackState);
        if (notificationBuilder == null) {
            notificationBuilder = new NotificationCompat
                    .Builder(serviceRef.get().getApplicationContext());
        }

        notificationBuilder
                .setVisibility(Notification.VISIBILITY_PUBLIC)
                .setCategory(Notification.CATEGORY_TRANSPORT)
                .setContentIntent(PendingIntent.getActivity(serviceRef.get(), 0,
                        new Intent(serviceRef.get(), MainActivity.class), 0))
                .setColor(ContextCompat.getColor(serviceRef.get().getApplicationContext(),
                        R.color.md_violet_custom_1))
                .setContentTitle(playbackStateRef.get().getState() == PlaybackStateCompat.STATE_PLAYING ?
                        "Playing music" : "Paused @@@@@@@@@")
                .setContentText(" - ")
                .setSmallIcon(R.drawable.sound_track_icon)
                .addAction(getActionDependingOnState(PlaybackState.STATE_REWINDING))
                .addAction(getActionDependingOnState(PlaybackState.STATE_PLAYING))
                .addAction(getActionDependingOnState(PlaybackState.STATE_FAST_FORWARDING))
                .setStyle(new NotificationCompat.MediaStyle()
                        .setMediaSession(mediaSessionRef.get().getSessionToken())
                        .setShowActionsInCompactView(new int[]{1})
                        .setShowCancelButton(true)
                        .setCancelButtonIntent(getPendingIntent(ACTION_STOP)));
        setNotificationPlaybackState(notificationBuilder);
        nm.notify(NOTIFICATION_ID, notificationBuilder.build());
    }

    /**
     *
     * @param mediaArtUri
     * @return
     */
    private void retrieveMediaArtAsync(Uri mediaArtUri) {
        if (mediaArtUri == null) {
            Log.e(TAG, "no media art required to be retrieved");
            return;
        }
        VolleyMediaArtManager.getInstance(new WeakReference<> (serviceRef.get().getApplicationContext()),
                new WeakReference<VolleyMediaArtManager.OnVolleyMediaArtCallbackInterface>(this))
                .retrieveMediaArtAsync(mediaArtUri);
    }

    /**
     *
     * SET compat view or expanded view
     *
     * @param builder
     */
    private void setNotificationPlaybackState(NotificationCompat.Builder builder) {
        if (playbackStateRef.get() == null) {
            Log.d(TAG, "updateNotificationPlaybackState. cancelling notification!");
            serviceRef.get().stopSelf();
            return;
        }

        boolean isPlaying = playbackStateRef.get().getState() == PlaybackStateCompat.STATE_PLAYING
                && playbackStateRef.get().getPosition() >= 0;
        builder
                .setWhen(isPlaying ? System.currentTimeMillis() - playbackStateRef.get().getPosition() : 0)
                .setShowWhen(isPlaying)
                .setUsesChronometer(isPlaying)
                .setOngoing(playbackStateRef.get().getState() == PlaybackStateCompat.STATE_PLAYING);
    }

    /**
     *
     * @return
     */
    public NotificationCompat.Action getActionDependingOnState(int state) {
        switch (state) {
            case PlaybackState.STATE_PLAYING:
            case PlaybackState.STATE_PAUSED:
                return playbackStateRef.get().getState() == PlaybackStateCompat.STATE_PLAYING ?
                        createAction(R.drawable.ic_action_pause, "Pause", ACTION_PAUSE) :
                        createAction(R.drawable.ic_action_play, "Play", ACTION_PLAY);
            case PlaybackState.STATE_FAST_FORWARDING:
                return createAction(R.drawable.ic_action_fast_forward, "Fast Forward", ACTION_FAST_FORWARD);
            case PlaybackState.STATE_REWINDING:
                return createAction(R.drawable.ic_action_rewind, "Rewind", ACTION_REWIND);
        }
        return null;
    }

    /**
     *
     * @param icon
     * @param text
     * @param action
     * @return
     */
    public NotificationCompat.Action createAction(int icon, String text, String action) {
        PendingIntent pendingIntent = getPendingIntent(action);
        return new NotificationCompat.Action.Builder(icon, text, pendingIntent)
                .build();
    }

    /**
     *
     */
    public void cancel() {
        nm.cancel(NOTIFICATION_ID);
    }

    /**
     *
     * @param action
     * @return
     */
    public PendingIntent getPendingIntent(String action) {
        Intent intent = new Intent(serviceRef.get(), MediaService.class);
        intent.setAction(action);
        return PendingIntent.getService(serviceRef.get().getApplicationContext(), 1, intent, 0);
    }

    /**
     *
     * @param playbackState
     */
    public void updatePlaybackCallbackState(WeakReference<PlaybackStateCompat> playbackState) {
        playbackStateRef = playbackState;
    }

    @Override
    public void onVolleyMediaArtSuccess(Bitmap response) {
        if (notificationBuilder != null) {
            Notification notification = notificationBuilder
                    .setLargeIcon(response)
                    .build();
            nm.notify(NOTIFICATION_ID, notification);
        }
    }

    @Override
    public void onVolleyMediaArtError(VolleyError error) {
        Log.e(TAG, "VOLLEY - " + error.getMessage());
    }
}
