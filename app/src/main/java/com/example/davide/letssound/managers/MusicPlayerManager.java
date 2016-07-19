package com.example.davide.letssound.managers;

import android.app.Activity;
import android.media.session.MediaController;
import android.util.Log;

import com.example.davide.letssound.helpers.ObservableHelper;
import com.example.davide.letssound.services.MediaService;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by davide on 15/07/16.
 */
public class MusicPlayerManager implements ObservableHelper.ObservableHelperInterface {
    private static final String TAG = "MusicPlayerManagerTAG";
    private static MusicPlayerManager instance;
    private static RetrofitYoutubeDownloaderManager retrofitManager;
    private static ObservableHelper observableHelper;
    private static WeakReference<MediaService> serviceRef;
    private static WeakReference<Activity> activityRef;

    public static MusicPlayerManager getInstance(WeakReference<Activity> activity,
                                                 WeakReference<MediaService> service) {
        if (instance == null) {
            instance = new MusicPlayerManager();
        }
        retrofitManager = RetrofitYoutubeDownloaderManager.getInstance();
        activityRef = activity;
        serviceRef = service;
        observableHelper = new ObservableHelper(new WeakReference<ObservableHelper.ObservableHelperInterface>(instance));
        return instance;
    }

    /**
     *
     * @param videoId
     */
    public void playSoundTrack(String videoId) {
        fetchSoundTrackUrl(videoId);
    }

    /**
     *
     * @param videoId
     */
    public void fetchSoundTrackUrl(String videoId) {
        Log.e(TAG, "fetch" + videoId);
        Observable<Object> obs = retrofitManager.fetchUrlByVideoId(videoId)
                .map(new Func1<String, Object>() {
            @Override
            public Object call(String s) {
                return s;
            }
        });
        observableHelper.initObservableObject(obs);
    }

    @Override
    public void onObservableSuccess(ArrayList<Object> list, String requestType) {
        //TODO fix it
        Log.e(TAG, "success music player LIST");
    }

    @Override
    public void onObservableSuccess(Object obj, String requestType) {
        Log.e(TAG, "success music player " + obj.toString());
        initMediaService((String) obj);
    }


    @Override
    public void onObservableEmpty() {

    }

    @Override
    public void onObservableError(String type, String error) {
        Log.e(TAG, "error" + error);
    }

    /**
     *
     */
    private void initMediaService(String url) {
        MediaService tmp = serviceRef.get();
        tmp.prepareMediaSessionToPlay(url); //MUST BE SYNC
        playMedia();
    }

    /**
     * play media by controller
     */
    private void playMedia() {
        MediaController controller = activityRef.get().getMediaController();
        if (controller != null) {
            controller.getTransportControls().play();
        }
    }

    /**
     * pause media by controller
     */
    private void pauseMedia() {
        MediaController controller = activityRef.get().getMediaController();
        if (controller != null) {
            controller.getTransportControls().pause();
        }
    }


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
