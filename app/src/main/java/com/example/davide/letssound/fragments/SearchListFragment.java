package com.example.davide.letssound.fragments;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.davide.letssound.BuildConfig;
import com.example.davide.letssound.R;
import com.example.davide.letssound.downloader.DownloadVolleyResponse.DownloadStatusEnum;
import com.example.davide.letssound.downloader.helper.DownloaderHelper;
import com.example.davide.letssound.downloader.helper.OnDownloadHelperResultInterface;
import com.example.davide.letssound.helpers.SoundTrackStatus;
import com.example.davide.letssound.adapters.SoundTrackRecyclerViewAdapter;
import com.example.davide.letssound.decorations.SimpleDividerItemDecoration;
import com.example.davide.letssound.downloader.DownloadVolleyResponse;
import com.example.davide.letssound.downloader.InputStreamVolleyRequest;
import com.example.davide.letssound.helpers.YoutubeConnector;
import com.google.api.client.util.DateTime;
import com.google.api.services.youtube.model.ResourceId;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.SearchResultSnippet;

import java.io.FileDescriptor;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import butterknife.Bind;
import butterknife.ButterKnife;
import icepick.Icepick;
import icepick.State;

import static com.example.davide.letssound.downloader.DownloadVolleyResponse.*;

/**
 * A placeholder fragment containing a simple view.
 */
public class SearchListFragment extends Fragment implements SoundTrackRecyclerViewAdapter.OnItemClickListenerInterface,
        SoundTrackRecyclerViewAdapter.OnClickListenerInterface, SearchView.OnQueryTextListener,
        MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, View.OnClickListener,
        SwipeRefreshLayout.OnRefreshListener, OnDownloadCallbackInterface, OnDownloadHelperResultInterface {
    @Bind(R.id.trackRecyclerViewId)
    RecyclerView soundTrackRecyclerView;
    @Bind(R.id.swipeContainerLayoutId)
    SwipeRefreshLayout swipeContainerLayout;

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private View mRootView;
    private SearchView searchView;
    private MediaPlayer mp;
    private MediaObserver observer;
    @State ArrayList<SearchResult> result;
    private View soundTrackPlayingBoxLayoutHeader;
    private TextView soundTrackTitleTextView;
    private View pauseButton;
    private View playButton;
    private SoundTrackStatus soundTrackStatus;
    private MenuItem searchMenuItem;
    private String downloadFilename;
    private DownloaderHelper downloaderHelper;
    private YoutubeConnector youtubeConnector;
    private TextView durationTimeActionbarTextView;
    private TextView currentTimeActionbarTextView;
    private SeekBar soundTrackSeekbar;
    private View downloadTextView;
    private View shareTextView;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static SearchListFragment newInstance(int sectionNumber) {
        SearchListFragment fragment = new SearchListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public SearchListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_search_list_layout, container, false);
        ButterKnife.bind(this, mRootView);
        initView(savedInstanceState);
        return mRootView;
    }

    /**
     *
     * @param savedInstanceState
     */
    public void initView(Bundle savedInstanceState) {
        soundTrackStatus = SoundTrackStatus.getInstance();
        downloaderHelper = DownloaderHelper.getInstance(new WeakReference<Activity>(getActivity()),
                new WeakReference<OnDownloadHelperResultInterface>(this));
        initSwipeRefresh();
        initResult();
        initProgressBar();
        setHasOptionsMenu(true);
        mp = new MediaPlayer();
        if (soundTrackStatus.isPlayStatus()) {
            onPlayingStatusPrepareUI();
        }

        if (savedInstanceState == null ||
            result.size() != 0) {
            initRecyclerView(result);
//            return;
        }

//        if (BuildConfig.DEBUG) {
//            //TODO test - rm it
//            fillList();
//        }
    }

    private void initSwipeRefresh() {
//        swipeContainerLayout.setVisibility(View.GONE);
        swipeContainerLayout.setOnRefreshListener(this);
    }

    /**
     * init result
     */
    private void initResult() {
        result = (result == null) ? new ArrayList<SearchResult>() : result;
    }

    /**
     *
     * @param searchResultList
     */
    private void createSearchResultList(List<SearchResult> searchResultList) {
        result.clear();
        result.addAll(searchResultList);
    }

    /**
     * init progress bar
     */
    private void initProgressBar() {
        soundTrackPlayingBoxLayoutHeader = getActivity()
                .findViewById(R.id.soundTrackPlayingBoxLayoutId);
        soundTrackPlayingBoxLayoutHeader.setVisibility(View.GONE);
        soundTrackSeekbar = (SeekBar) soundTrackPlayingBoxLayoutHeader
                .findViewById(R.id.soundTrackSeekbarId);
        durationTimeActionbarTextView = (TextView) soundTrackPlayingBoxLayoutHeader
                .findViewById(R.id.durationTimeActionbarTextId);
        currentTimeActionbarTextView = (TextView) soundTrackPlayingBoxLayoutHeader
                .findViewById(R.id.currentTimeActionbarTextId);
        soundTrackTitleTextView = (TextView) soundTrackPlayingBoxLayoutHeader
                .findViewById(R.id.soundTrackTitleTextId);
        pauseButton = getActivity()
                .findViewById(R.id.pausePlayingBoxButtonId);
        playButton = getActivity()
                .findViewById(R.id.playPlayingBoxButtonId);
        shareTextView = soundTrackPlayingBoxLayoutHeader
                .findViewById(R.id.shareTextId);
        downloadTextView = soundTrackPlayingBoxLayoutHeader
                .findViewById(R.id.downloadTextId);

        shareTextView.setOnClickListener(this);
        downloadTextView.setOnClickListener(this);
        pauseButton.setOnClickListener(this);
        playButton.setOnClickListener(this);
        initPlayerUI();

        int color = getActivity().getResources().getColor(R.color.md_violet_custom_0);
        soundTrackSeekbar.getProgressDrawable().setColorFilter(color, PorterDuff.Mode.SRC_IN);
        soundTrackSeekbar.getThumb().setColorFilter(color, PorterDuff.Mode.SRC_IN);
    }


    @Override
    public void onItemClick(int position, View v) {
//        setOnSelectedStatus(position);
        playSoundTrackWrapper(position);
    }

    @Override
    public void onClick(int position, View v) {
//        switch (v.getId()) {
//            case R.id.playButtonId:
//                playSoundTrackWrapper(position);
//                break;
//            case R.id.pauseButtonId:
//                mp.pause();
//                break;
//
//        }
    }

    /**
     *
     */
    public void initPlayerUI() {
        pauseButton.setVisibility(View.GONE);
        playButton.setVisibility(View.GONE);
    }

    /**
     *
     * @param isPlaying
     */
    public void setPlayPlayerUI(boolean isPlaying) {
        pauseButton.setVisibility(isPlaying ? View.VISIBLE : View.GONE);
        playButton.setVisibility(isPlaying ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pausePlayingBoxButtonId:
                mp.pause();
                setPlayPlayerUI(false);
                break;
            case R.id.playPlayingBoxButtonId:
                mp.start();
                setPlayPlayerUI(true);
                break;
            case R.id.downloadTextId:
                try {
                    int pos = soundTrackStatus.getCurrentPosition();
                    if (!soundTrackStatus.isValidPosition(pos)) {
                        handleError(new Exception("Song position not valid - please select it again"));
                        break;
                    }
                    sendSnackbarMessage("hey download position " + pos);
                    swipeContainerLayout.setRefreshing(true);
                    downloadSoundTrack(pos);
                } catch (Exception e) {
                    swipeContainerLayout.setRefreshing(false);
                    e.printStackTrace();
                }
                break;
            case R.id.shareTextId:
                int pos = soundTrackStatus.getCurrentPosition();
                if (!soundTrackStatus.isValidPosition(pos)) {
                    handleError(new Exception("Song position not valid - please select it again"));
                    break;
                }

                sendSnackbarMessage("hey share position " + pos);
                shareSoundTrack(pos);
                break;
        }
    }

    /**
     *
      * @param position
     */
    private void shareSoundTrack(int position) {
        SearchResult obj = getTrackByPosition(position);
        String videoId = obj.getId().getVideoId();
        String title = obj.getSnippet().getTitle();
        String content = "[Video URL]: \n http://youtube.com/?v=" + videoId;
        shareSoundTrackByIntent(title, content);
    }

    /**
     *
     * @param title
     * @param content
     */
    private void shareSoundTrackByIntent(String title, String content) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, title);
        shareIntent.putExtra(Intent.EXTRA_TEXT, content);
        startActivity(Intent.createChooser(shareIntent, "Share sound track:"));
    }

    /**
     *
     * @param message
     */
    private void sendSnackbarMessage(String message) {
        Snackbar.make(mRootView, message, Snackbar.LENGTH_SHORT)
                .show();
    }

    /**
     *
     * @param e
     */
    @Override
    public void handleError(Exception e) {
        swipeContainerLayout.setRefreshing(false);
        setOnIdleStatus();
        createSnackBarByBackgroundColor(e.getMessage(), getActivity()
                .getResources()
                .getColor(R.color.md_red_400));
    }

    @Override
    public void handleSuccess(String message) {
        createSnackBarByBackgroundColor(message, getActivity()
                .getResources()
                .getColor(R.color.md_teal_400));
    }

    /**
     *
     * @param message
     * @param color
     */
    private void createSnackBarByBackgroundColor(String message, int color) {
        Snackbar snackbar = Snackbar
                .make(mRootView, message, Snackbar.LENGTH_SHORT);
        snackbar.getView().setBackgroundColor(color);
        snackbar.show();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        searchMenuItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) MenuItemCompat.getActionView(searchMenuItem);
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Activity.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setOnQueryTextListener(this);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                stopPlaying();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        searchMenuItem.collapseActionView();
        soundTrackStatus.setIdleStatus();
        searchOnYoutubeAsync(query);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    /**
     *
     */
    public void stopPlaying() {
        initPlayerUI();
        setOnPlayingActionbar(false);
        setOnIdleStatus();
        //reset play pos
        setOnPlayingStatus(SoundTrackStatus.INVALID_POSITION);
//        setOnSelectedStatus(SoundTrackStatus.INVALID_POSITION);
        stopMediaPlayer();
    }
    /**
     * @deprecated
     * @param query
     */
    private void setResultOnSubmitQuery(String query) {
        List<SearchResult> list = youtubeConnector
                .search(query);
        if (list == null) {
            handleError(new Exception("No result found"));
            return;
        }
        createSearchResultList(list);
        initRecyclerView(result);
    }

    /**
     *
     * @param queryString
     */
    public void searchOnYoutubeAsync(final String queryString) {
        final Handler handler = new Handler();
        new Thread(){
            public void run(){
                youtubeConnector = YoutubeConnector.getInstance();
                final List<SearchResult> list = youtubeConnector.search(queryString);
                handler.post(new Runnable(){
                    public void run(){
                        if (list == null) {
                            handleError(new Exception("No result found"));
                            return;
                        }
                        createSearchResultList(list);
                        initRecyclerView(result);
                    }
                });
            }
        }.start();
    }

    /**
     *
     * @param result
     */
    private void initRecyclerView(final ArrayList<SearchResult> result) {
        final SoundTrackRecyclerViewAdapter.OnItemClickListenerInterface fragmentRef = this;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                soundTrackRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
                soundTrackRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                SoundTrackRecyclerViewAdapter adapter =
                        new SoundTrackRecyclerViewAdapter(result, fragmentRef);
                soundTrackRecyclerView.setAdapter(adapter);
            }

        });

    }

    /**
     * @deprecated
     */
    private void fillList() {
        ArrayList<SearchResult> result = new ArrayList<>();
        result.add(createSearchResultObj("hey"));
        result.add(createSearchResultObj("res"));
        result.add(createSearchResultObj("bla"));
        result.add(createSearchResultObj("hey"));
        result.add(createSearchResultObj("res"));
        result.add(createSearchResultObj("bla"));
        result.add(createSearchResultObj("hey"));
        result.add(createSearchResultObj("res"));
        result.add(createSearchResultObj("bla"));
        initRecyclerView(result);
    }

    /**
     *
     * @param title
     * @return
     */
    private SearchResult createSearchResultObj(String title) {
        SearchResult obj = new SearchResult();
        SearchResultSnippet snippet = new SearchResultSnippet();
        snippet.setTitle(title);
        snippet.setPublishedAt(new DateTime(new Date()));
        obj.setKind("John Bleam");
        obj.setId(new ResourceId().setVideoId("blaaaaaaurl bla claaaa"));
        obj.setSnippet(snippet);
        return obj;
    }

    //MediaPlayer songs
    /**
     * 
     * @param position
     */
    private void downloadSoundTrack(int position) throws Exception {
        SearchResult obj = getTrackByPosition(position);
        String videoId = obj.getId().getVideoId();
        downloadFilename = obj.getSnippet().getTitle();

        setOnDownloadStatus();
        downloaderHelper.retrieveSoundTrackAsync(videoId, null);
//        retrieveVideoUrlAsync(videoId, Long.toString(getTimestamp()));
    }

    /**
     * @param url
     */
    public void downloadSoundTrackByUrl(String url) {
        DownloadVolleyResponse volleyResponse = new DownloadVolleyResponse(downloadFilename,
                new WeakReference<OnDownloadCallbackInterface>(this));
//        RequestQueue mRequestQueue = Volley.newRequestQueue(getActivity().getApplicationContext(),
//                new HurlStack());
//        mRequestQueue.add(new InputStreamVolleyRequest(Request.Method.GET, url,
//                volleyResponse, volleyResponse, null));
    }
    /**
     *
     */
    private void setOnDownloadStatus() {
        soundTrackStatus.setDownloadStatus();
    }

    /**
     *
     * @param position
     */
    private void playSoundTrackWrapper(int position) {
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
    private void playSoundTrack(int position) throws Exception {
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
    private void initMediaPlayer() {
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
    private SearchResult getTrackByPosition(int position) {
        return ((SoundTrackRecyclerViewAdapter) soundTrackRecyclerView.getAdapter())
                .getItemByPosition(position);
    }

    /**
     *
     * @param url
     * @throws IOException
     * @throws Exception
     */
    public void startMediaPlayer(String url) throws Exception {
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
    private void startMediaPlayer(FileDescriptor fd) throws Exception {
        mp.reset();
        mp.setOnErrorListener(this);
        mp.setOnPreparedListener(this);
        mp.setDataSource(fd);
        mp.prepareAsync();
    }

    /**
     *
     */
    private void stopMediaPlayer() {
        if (mp.isPlaying()) {
            mp.stop();
        }
        mp.reset();
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        setPlayPlayerUI(true);
        onPlayingStatusPrepareUI();
        mp.start();
    }

    /**
     *
     */
    private void onPlayingStatusPrepareUI() {
        swipeContainerLayout.setRefreshing(false);
        setOnPlayingActionbar(true);
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        mp.reset();
        return false;
    }

    /**
     *
     * @return
     */
    public long getTimestamp() {
        return System.currentTimeMillis();
    }

    /**
     *
     * @param url
     * @throws IOException
     */
    public void playSoundTrackFromStorage(String url) throws IOException {
        Log.e("TAG", url);
        mp.setDataSource(url);
        mp.prepare();
    }

    /**
     *
     */
    private void setOnSelectedActionbar(final boolean isSelected) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setTopActionbar(isSelected);
                getActivity().findViewById(R.id.slidingTabsId).setVisibility(isSelected ?
                        View.GONE : View.VISIBLE);
                soundTrackPlayingBoxLayoutHeader.setVisibility(isSelected ?
                        View.VISIBLE : View.GONE);

            }
        });
    }
    /**
     *
     * @param isPlaying
     */
    private void setOnPlayingActionbar(final boolean isPlaying) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //header status
                setTopActionbar(isPlaying);
                getActivity().findViewById(R.id.slidingTabsId).setVisibility(isPlaying ?
                        View.GONE : View.VISIBLE);
                soundTrackPlayingBoxLayoutHeader.setVisibility(isPlaying ?
                        View.VISIBLE : View.GONE);
            }
        });
    }

    /**
     *
     * @param isPlaying
     */
    private void setTopActionbar(boolean isPlaying) {
//        Log.e("TAG", "hey playing" + isPlaying);
        if (searchMenuItem != null) {
            searchMenuItem.setVisible(!isPlaying);
        }

        try {
//            Resources resources = getActivity().getResources();
//            int color = resources.getColor(isPlaying ?
//                    R.color.md_red_custom_1 : R.color.md_yellow_custom_1);
            ActionBar actionbar = ((AppCompatActivity) getActivity())
                    .getSupportActionBar();
//            actionbar.setBackgroundDrawable(new ColorDrawable(color));
            actionbar.setDisplayHomeAsUpEnabled(isPlaying);
            actionbar.setDisplayShowTitleEnabled(!isPlaying);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     *
     * @param pos
     */
    private void setOnPlayingStatus(final int pos) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (soundTrackStatus.isValidPosition(pos)) {
                    soundTrackStatus.setPlayStatus();
                }
                soundTrackStatus.setCurrentPosition(pos);
                soundTrackRecyclerView.getAdapter().notifyDataSetChanged();
            }
        });
    }

    /**
     * @param pos
     */
    private void setOnSelectedStatus(int pos) {
        soundTrackStatus.setSelectStatus();
        soundTrackStatus.setCurrentPosition(pos);
        soundTrackRecyclerView.getAdapter().notifyDataSetChanged();
        setOnSelectedActionbar(true);

        SearchResult obj = ((SoundTrackRecyclerViewAdapter) soundTrackRecyclerView.getAdapter())
                .getItemByPosition(pos);
        soundTrackTitleTextView.setText(obj.getSnippet().getTitle());
    }


    /**
     *
     */
    private void setOnIdleStatus() {
        //TODO crash
        try {
            soundTrackStatus.setCurrentPosition(SoundTrackStatus.INVALID_POSITION);
            soundTrackRecyclerView.getAdapter().notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

    @Override
    public void onRefresh() {
        swipeContainerLayout.setRefreshing(false);
    }

    @Override
    public void onDownloadCallback(DownloadStatusEnum statusEnum, Exception e) {
        if (statusEnum == DownloadStatusEnum.FAILED) {
            handleError(e);
        } else if (statusEnum == DownloadStatusEnum.OK) {
            swipeContainerLayout.setRefreshing(false);
            soundTrackStatus.setIdleStatus();
            soundTrackRecyclerView.getAdapter().notifyDataSetChanged();
            handleSuccess("Hey you got your song!");
        }
    }

    /**
     * @deprecated
     * @param query
     */
    public void setAsyncResultOnSubmitQuery(final String query) {
        String[] queryArray = {query};
        new AsyncTask<String, Boolean, Boolean>() {
            @Override
            protected Boolean doInBackground(String... params) {
                setResultOnSubmitQuery(params[0]);
                return true;
            }
        }.execute(queryArray);

    }

    /***
     * TODO move out
     */
    private class MediaObserver implements Runnable {
        private AtomicBoolean stop = new AtomicBoolean(false);

        public void stop() {
            stop.set(true);
        }

        /**
         *
         * @param millisec
         * @return
         */
        public String parseMillisecToString(long millisec) {
            return String.format(Locale.getDefault(), "%02d:%02d",
                    TimeUnit.MILLISECONDS.toMinutes(millisec),
                    TimeUnit.MILLISECONDS.toSeconds(millisec) -
                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisec)));
        }

        /**
         *
         */
        private void updateView() {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    currentTimeActionbarTextView.setText(parseMillisecToString(mp.getCurrentPosition()));
                    durationTimeActionbarTextView.setText(parseMillisecToString(mp.getDuration()));
                }
            });
        }

        @Override
        public void run() {
            while (!stop.get()) {
                if (mp.isPlaying()) {
                    try {
                        float progress = ((float) mp.getCurrentPosition() / mp.getDuration()) * 100;
                        updateView();
                        soundTrackSeekbar.setProgress((int) progress);
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        handleError(e);
                    }
                }
            }
            setOnPlayingActionbar(false);
            setOnPlayingStatus(SoundTrackStatus.INVALID_POSITION);
        }

    }


}