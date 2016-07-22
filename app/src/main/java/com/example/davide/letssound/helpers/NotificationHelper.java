package com.example.davide.letssound.helpers;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.session.MediaSession;
import android.media.session.PlaybackState;

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

    private final WeakReference<MediaService> serviceRef;
    private final WeakReference<PlaybackState> playbackStateRef;
    private final WeakReference<MediaSession> mediaSessionRef;
    //    private PlaybackState playbackState;
//    private MediaSession mediaSession;
    private NotificationManager nm;

    public NotificationHelper(WeakReference<MediaService> srv, WeakReference<MediaSession> ms,
                              WeakReference<PlaybackState> ps) {
        serviceRef = srv;
        mediaSessionRef = ms;
        playbackStateRef = ps;
        nm = (NotificationManager) serviceRef.get().getSystemService(Context.NOTIFICATION_SERVICE);
    }

    /**
     *
     */
    public void updateNotification() {
        PendingIntent contentIntent = PendingIntent.getActivity(serviceRef.get(), 0, new Intent(serviceRef.get(), MainActivity.class), 0);
        Notification notification = new Notification.Builder(serviceRef.get())
                .setPriority(Notification.PRIORITY_DEFAULT)
                .setVisibility(Notification.VISIBILITY_PUBLIC)
                .setCategory(Notification.CATEGORY_TRANSPORT)
                .setContentIntent(contentIntent)
                .setContentTitle("Cubs The Favorites?: 10/14/15")
                .setContentText("ESPN: PTI")
                .setOngoing(playbackStateRef.get().getState() == PlaybackState.STATE_PLAYING)
                .setShowWhen(false)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(false)
                .addAction(getActionDependingOnState(PlaybackState.STATE_REWINDING))
                .addAction(getActionDependingOnState(PlaybackState.STATE_PLAYING))
                .addAction(getActionDependingOnState(PlaybackState.STATE_FAST_FORWARDING))
                .setStyle(new Notification.MediaStyle()
                        .setMediaSession(mediaSessionRef.get().getSessionToken())
                        .setShowActionsInCompactView(1, 2))
                .build();
        nm.notify(NOTIFICATION_ID, notification);
    }

    /**
     *
     * @return
     */
    public Notification.Action getActionDependingOnState(int state) {
        switch (state) {
            case PlaybackState.STATE_PLAYING:
            case PlaybackState.STATE_PAUSED:
                return playbackStateRef.get().getState() == PlaybackState.STATE_PLAYING ?
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
    public Notification.Action createAction(int icon, String text, String action) {
        Intent intent = new Intent(serviceRef.get(), MediaService.class);
        intent.setAction(action);
        PendingIntent pendingIntent = PendingIntent.getService(serviceRef.get().getApplicationContext(), 1, intent, 0);
        return new Notification.Action.Builder(icon, text, pendingIntent)
                .build();
    }

    public void cancel() {
        nm.cancel(NOTIFICATION_ID);
    }
}
