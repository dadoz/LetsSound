package com.example.davide.letssound.helpers;

import android.media.MediaMetadata;
import android.media.session.MediaSession;

import java.util.ArrayList;

public class MediaSessionQueueHelper {
    private static MediaSessionQueueHelper instance;
    private final ArrayList<MediaSession.QueueItem> itemQueued;

    public static MediaSessionQueueHelper getInstance() {
        return instance == null ? instance = new MediaSessionQueueHelper() : instance;
    }

    private MediaSessionQueueHelper() {
        itemQueued = new ArrayList<>();

    }

    /**
     *
     * @return
     */
    public ArrayList<MediaSession.QueueItem> getQueue() {
        return itemQueued;
    }

    /**
     *
     * @return
     */
    public ArrayList<MediaSession.QueueItem> addItemOnQueue(String url) {
        MediaMetadata mediaMetadata = new MediaMetadata.Builder()
                .putString("metadata", url)
                .build();
        long trackId = 0;
        MediaSession.QueueItem queueItem = new MediaSession
                .QueueItem(mediaMetadata.getDescription(), trackId);
        itemQueued.add(queueItem);
        return itemQueued;
    }

    /**
     *
     * @param id
     * @return
     */
    public MediaSession.QueueItem getItemQueuedd(int id) {
        return itemQueued.get(id);
    }
}
