package com.application.letssound.network.downloader.helper;

/**
 * Created by davide on 01/01/16.
 */
public interface OnDownloadHelperResultInterface {
    void handleError(Exception error);
    void handleSuccess(String message);
    void startMediaPlayer(String url) throws Exception;
    void downloadSoundTrackByUrl(String url);
}
