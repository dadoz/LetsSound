package com.application.letssound.services.notifications;

import com.application.letssound.services.MusicService;

/**
 * Created by davide-syn on 10/3/17.
 */

public interface PlayingNotification {
    void init(MusicService musicService);

    void update();

    void stop();
}
