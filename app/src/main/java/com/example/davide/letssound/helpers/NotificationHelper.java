package com.example.davide.letssound.helpers;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.media.session.PlaybackState;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.ContextCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.app.NotificationCompat;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.TypefaceSpan;
import android.util.Log;

import com.android.volley.VolleyError;
import com.example.davide.letssound.MainActivity;
import com.example.davide.letssound.R;
import com.example.davide.letssound.SoundTrackPlayerActivity;
import com.example.davide.letssound.managers.MusicPlayerManager;
import com.example.davide.letssound.managers.VolleyMediaArtManager;
import com.example.davide.letssound.models.Setting;
import com.example.davide.letssound.services.MediaService;
import com.example.davide.letssound.utils.Utils;

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
    private Uri mediaArtUri;
    private Uri soundTrackUri;
    private String title;

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
    public void updateNotification(WeakReference<PlaybackStateCompat> playbackState, String title,
                                   Uri soundTrackUri, Uri mediaArtUri) {
        retrieveMediaArtAsync(mediaArtUri);
        updatePlaybackCallbackState(playbackState);
        setSoundTrackInfo(title, soundTrackUri, mediaArtUri);
        notificationBuilder = new NotificationCompat.Builder(serviceRef.get().getApplicationContext());
        notificationBuilder
                .setVisibility(Notification.VISIBILITY_PUBLIC)
                .setCategory(Notification.CATEGORY_TRANSPORT)
                .setContentIntent(getContentIntent())
                .setColor(ContextCompat.getColor(serviceRef.get().getApplicationContext(),
                        R.color.md_blue_grey_800))
                .setContentTitle(title)
                .setContentText(" - ")
                .setSmallIcon(R.drawable.ic_music_note_black_48dp)
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
     * @param title
     * @param soundTrackUri
     * @param mediaArtUri
     */
    private void setSoundTrackInfo(String title, Uri soundTrackUri, Uri mediaArtUri) {
        this.title = title;
        this.soundTrackUri = soundTrackUri;
        this.mediaArtUri = mediaArtUri;
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
     * @return
     */
    public PendingIntent getContentIntent() {
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(serviceRef.get());
        stackBuilder.addParentStack(SoundTrackPlayerActivity.class);
        stackBuilder.addNextIntent(getNextIntent());
        return stackBuilder.getPendingIntent(1, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    /**
     *
     * @return
     */
    public Intent getNextIntent() {
        Intent resultIntent = new Intent(serviceRef.get(), SoundTrackPlayerActivity.class);
        if (soundTrackUri != null &&
                mediaArtUri != null &&
                title != null) {
            resultIntent.putExtras(Utils
                    .buildPlayBundle(soundTrackUri.toString(), mediaArtUri.toString(), title));
        }
        return resultIntent;
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
    public void onVolleyMediaArtSuccess(Bitmap response, String requestedUrl) {
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
