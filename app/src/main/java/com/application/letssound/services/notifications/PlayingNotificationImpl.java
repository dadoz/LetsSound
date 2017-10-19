package com.application.letssound.services.notifications;

import com.application.letssound.services.MusicService;

/**
 * Created by davide-syn on 10/3/17.
 */

public class PlayingNotificationImpl implements PlayingNotification {
    private MusicService musicService;

    public void init(MusicService musicService) {
        this.musicService = musicService;
    }

}
