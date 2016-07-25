package com.example.davide.letssound.helpers;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.session.MediaSession;
import android.media.session.PlaybackState;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.app.NotificationCompat;

import com.example.davide.letssound.MainActivity;
import com.example.davide.letssound.R;
import com.example.davide.letssound.services.MediaService;

import java.lang.ref.WeakReference;

/**
 * Created by davide on 22/07/16.
 */
public class NotificationHelper {
    private final static int NOTIFICATION_ID = 999999;

    public static final String ACTION_PLAY = "play";
    public static final String ACTION_PAUSE = "pause";
    public static final String ACTION_FAST_FORWARD = "fastForward";
    public static final String ACTION_REWIND = "rewind";
    public static final String ACTION_STOP = "stop";

    private final WeakReference<MediaService> serviceRef;
    private final WeakReference<PlaybackStateCompat> playbackStateRef;
    private final WeakReference<MediaSessionCompat> mediaSessionRef;
    //    private PlaybackState playbackState;
//    private MediaSession mediaSession;
    private NotificationManagerCompat nm;

    public NotificationHelper(WeakReference<MediaService> srv, WeakReference<MediaSessionCompat> ms,
                              WeakReference<PlaybackStateCompat> ps) {
        serviceRef = srv;
        mediaSessionRef = ms;
        playbackStateRef = ps;
//        nm = (NotificationHelper) serviceRef.get().getSystemService(Context.NOTIFICATION_SERVICE);
        nm = NotificationManagerCompat.from(serviceRef.get());
    }

    /**
     *
     */
    public void updateNotification() {
//        int notificationColor = ResourceHelper.getThemeColor(serviceRef.get(),
//                android.R.attr.colorPrimary, Color.DKGRAY);

        PendingIntent contentIntent = PendingIntent.getActivity(serviceRef.get(), 0, new Intent(serviceRef.get(), MainActivity.class), 0);
        Notification notification = new NotificationCompat.Builder(serviceRef.get())
                .setVisibility(Notification.VISIBILITY_PUBLIC)
                .setCategory(Notification.CATEGORY_TRANSPORT)
                .setContentIntent(contentIntent)
                .setContentTitle(" - ")
                .setContentText(" - ")
//                .setColor(notificationColor)
                .setOngoing(playbackStateRef.get().getState() != PlaybackStateCompat.STATE_PLAYING)
                .setWhen(0)
                .setShowWhen(false)
                .setSmallIcon(R.drawable.sound_track_icon)
                .addAction(getActionDependingOnState(PlaybackState.STATE_REWINDING))
                .addAction(getActionDependingOnState(PlaybackState.STATE_PLAYING))
                .addAction(getActionDependingOnState(PlaybackState.STATE_FAST_FORWARDING))
                .setStyle(new NotificationCompat.MediaStyle()
                        .setMediaSession(mediaSessionRef.get().getSessionToken())
                        .setShowActionsInCompactView(new int[] {1})
                        .setShowCancelButton(true)
                        .setCancelButtonIntent(getPendingIntent(ACTION_STOP)))//getActionDependingOnState(PlaybackState.STATE_STOPPED))
//                        .setShowActionsInCompactView(1, 2))
                .build();
        nm.notify(NOTIFICATION_ID, notification);
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
}
