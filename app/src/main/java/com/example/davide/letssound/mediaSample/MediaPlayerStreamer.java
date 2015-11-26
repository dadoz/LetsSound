package com.example.davide.letssound.mediaSample;

import android.media.AudioManager;
import android.media.MediaPlayer;

/**
 * Created by davide on 20/11/15.
 */
public class MediaPlayerStreamer {
    //get sample on https://github.com/googlesamples/android-MediaBrowserService

    public void streamByUrl(String url) {
        url = "http://xty/MRESC/images/test/xy.mp3";
        try {
            MediaPlayer player = new MediaPlayer();
            player.setAudioStreamType(AudioManager.STREAM_MUSIC);
            player.setDataSource(url);
            player.prepare();
            player.start();

        } catch (Exception e) {
            // TODO: handle exception
        }

    }
}
