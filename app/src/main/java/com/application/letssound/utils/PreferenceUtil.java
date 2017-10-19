package com.application.letssound.utils;

import com.application.letssound.services.MusicService;

/**
 * Created by davide-syn on 10/3/17.
 */

public class PreferenceUtil {
    public static final String GAPLESS_PLAYBACK = "";
    public static final String ALBUM_ART_ON_LOCKSCREEN = "-";
    public static final String BLURRED_ALBUM_ART = "--";
    public static final String COLORED_NOTIFICATION = "---";

    public static PreferenceUtil getInstance(MusicService musicService) {
        return new PreferenceUtil();
    }

    public boolean albumArtOnLockscreen() {
        return false;
    }

    public boolean blurredAlbumArt() {
        return false;
    }

    public boolean audioDucking() {
        return false;
    }

    public void unregisterOnSharedPreferenceChangedListener(MusicService musicService) {

    }
}
