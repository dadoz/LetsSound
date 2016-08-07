package com.application.letssound.downloader.helper;

/**
 * Created by davide on 01/01/16.
 */
public interface DownloaderInterface {
    void retrieveSoundTrackAsync(final String videoId, String timestamp);
}
