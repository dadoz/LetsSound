package com.example.davide.letssound.managers;

import android.media.MediaPlayer;
import android.util.Log;
import android.view.View;

import com.example.davide.letssound.BuildConfig;
import com.example.davide.letssound.adapters.SoundTrackRecyclerViewAdapter;
import com.example.davide.letssound.helpers.SoundTrackStatus;
import com.example.davide.letssound.observer.MediaObserver;
import com.google.api.services.youtube.model.SearchResult;

import java.io.FileDescriptor;
import java.io.IOException;

/**
 * Created by davide on 15/07/16.
 */
public class MusicPlayerHelper {
    /**
     *
     */
/*    public void initPlayerUI() {
        pauseButton.setVisibility(View.GONE);
        playButton.setVisibility(View.GONE);
    }

    /**
     *
     * @param isPlaying
     */
/*    public void setPlayPlayerUI(boolean isPlaying) {
        pauseButton.setVisibility(isPlaying ? View.VISIBLE : View.GONE);
        playButton.setVisibility(isPlaying ? View.GONE : View.VISIBLE);
    }

    /**
     *
     */
/*    public void stopPlaying() {
        initPlayerUI();
        setOnPlayingActionbar(false);
        setOnIdleStatus();
        //reset play pos
        setOnPlayingStatus(SoundTrackStatus.INVALID_POSITION);
//        setOnSelectedStatus(SoundTrackStatus.INVALID_POSITION);
        stopMediaPlayer();
    }

    /**
     *
     * @param position
     */
/*    private void playSoundTrackWrapper(int position) {
        try {
            swipeContainerLayout.setRefreshing(true);
            setOnIdleStatus();
            playSoundTrack(position);
        } catch (Exception e) {
            mp.reset();
            handleError(e);
            e.printStackTrace();
        }
    }
    /**
     *
     * @param position
     */
/*    private void playSoundTrack(int position) throws Exception {
        initMediaPlayer();
        SearchResult obj = getTrackByPosition(position);
        String videoId = obj.getId().getVideoId();
        soundTrackTitleTextView.setText(obj.getSnippet().getTitle());

        setOnPlayingStatus(position);
        if (BuildConfig.DEBUG) {
            testSoundTrackPlay();
            return;
        }
        downloaderHelper.retrieveSoundTrackAsync(videoId, null);
    }

    private void testSoundTrackPlay() throws Exception {
        FileDescriptor fd = getActivity().getAssets().openFd("sample.mp3").getFileDescriptor();
        startMediaPlayer(fd);
    }

    /**
     * init mediampleayer listener
     */
/*    private void initMediaPlayer() {
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                observer.stop();
                soundTrackSeekbar.setProgress(mediaPlayer.getCurrentPosition());
            }
        });
        observer = new MediaObserver();
        new Thread(observer).start();

    }

    /**
     *
     * @param position
     * @return
     */
/*    private SearchResult getTrackByPosition(int position) {
        return ((SoundTrackRecyclerViewAdapter) soundTrackRecyclerView.getAdapter())
                .getItemByPosition(position);
    }

    /**
     *
     * @param url
     * @throws IOException
     * @throws Exception
     */
/*    public void startMediaPlayer(String url) throws Exception {
        Log.e("TAG", url);
        mp.reset();
        mp.setOnErrorListener(this);
        mp.setOnPreparedListener(this);
        mp.setDataSource(url);
        mp.prepareAsync();
    }
    /**
     *
     * @param fd
     * @throws IOException
     * @throws Exception
     */
/*    private void startMediaPlayer(FileDescriptor fd) throws Exception {
        mp.reset();
        mp.setOnErrorListener(this);
        mp.setOnPreparedListener(this);
        mp.setDataSource(fd);
        mp.prepareAsync();
    }

    /**
     *
     */
/*    private void stopMediaPlayer() {
        if (mp.isPlaying()) {
            mp.stop();
        }
        mp.reset();
    }

    /**
     *
     * @param url
     * @throws IOException
     */
/*    public void playSoundTrackFromStorage(String url) throws IOException {
        Log.e("TAG", url);
        mp.setDataSource(url);
        mp.prepare();
    }
*/
}
