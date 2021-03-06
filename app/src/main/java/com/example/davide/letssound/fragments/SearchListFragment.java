package com.example.davide.letssound.fragments;

/**
 * Created by davide on 26/11/15.
 */

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
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
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.davide.letssound.R;
import com.example.davide.letssound.downloader.DownloadVolleyResponse.DownloadStatusEnum;
import com.example.davide.letssound.singleton.SoundTrackStatus;
import com.example.davide.letssound.adapters.SoundTrackRecyclerViewAdapter;
import com.example.davide.letssound.decorations.SimpleDividerItemDecoration;
import com.example.davide.letssound.downloader.DownloadVolleyResponse;
import com.example.davide.letssound.downloader.InputStreamVolleyRequest;
import com.example.davide.letssound.singleton.YoutubeIntegratorSingleton;
import com.google.api.client.util.DateTime;
import com.google.api.services.youtube.model.ResourceId;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.SearchResultSnippet;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileDescriptor;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
        SwipeRefreshLayout.OnRefreshListener, OnDownloadCallbackInterface {
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
    private ProgressBar soundTrackProgressbar;
    private TextView soundTrackTitleTextView;
    private View pauseButton;
    private View playButton;
    private SoundTrackStatus soundTrackStatus;
    private MenuItem searchMenuItem;
    private String downloadFilename;

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
        initSwipeRefresh();
        initResult();
        initProgressBar();
        setHasOptionsMenu(true);
        mp = new MediaPlayer();
        pauseButton.setOnClickListener(this);
        playButton.setOnClickListener(this);
        if (soundTrackStatus.isPlayStatus()) {
            onPlayingStatusPrepareUI();
        }

        if (savedInstanceState == null ||
            result.size() != 0) {
            initRecyclerView(result);
            return;
        }
        //TODO test - rm it
//        fillList();
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
        soundTrackProgressbar = (ProgressBar) soundTrackPlayingBoxLayoutHeader
                .findViewById(R.id.soundTrackProgressbarId);
        soundTrackTitleTextView = (TextView) soundTrackPlayingBoxLayoutHeader
                .findViewById(R.id.soundTrackTitleTextId);
        pauseButton = soundTrackPlayingBoxLayoutHeader
                .findViewById(R.id.pausePlayingBoxButtonId);
        playButton = soundTrackPlayingBoxLayoutHeader
                .findViewById(R.id.playPlayingBoxButtonId);

        int color = getActivity().getResources().getColor(R.color.md_blue_grey_600);
        soundTrackProgressbar.getProgressDrawable().setColorFilter(color, PorterDuff.Mode.SRC_IN);
    }


    @Override
    public void onItemClick(int position, View v) {
        setOnSelectedStatus(position);
    }

    @Override
    public void onClick(int position, View v) {
        switch (v.getId()) {
            case R.id.playButtonId:
                swipeContainerLayout.setRefreshing(true);
                setOnIdleStatus();
                sendSnackbarMessage("hey click play - " + position);
                try {
                    playSoundTrack(position);
                } catch (Exception e) {
                    swipeContainerLayout.setRefreshing(false);
                    e.printStackTrace();
                    handleError(e);
                    mp.reset();
                }
                break;
            case R.id.pauseButtonId:
                mp.pause();
                break;
            case R.id.downloadTextId:
                try {
                    sendSnackbarMessage("hey download position " + position);
                    swipeContainerLayout.setRefreshing(true);
                    downloadSoundTrack(position);
                } catch (Exception e) {
                    swipeContainerLayout.setRefreshing(false);
                    e.printStackTrace();
                }
                break;
            case R.id.shareTextId:
                sendSnackbarMessage("hey share position " + position);
                shareSoundTrack(position);
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
        String content = "[Video URL]: \n http://youtube.com/?q=" + videoId;
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
    private void handleError(Exception e) {
        swipeContainerLayout.setRefreshing(false);
        setOnIdleStatus();
        createSnackBarByBackgroundColor(e.getMessage(), getActivity()
                .getResources()
                .getColor(R.color.md_red_400));
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
        searchView.setIconified(true);
        setResultOnSubmitQuery(query);
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
        setOnPlayingActionbar(false);
        setOnPlayingStatus(-1);
        stopMediaPlayer();
    }
    /**
     *
     * @param query
     */
    private void setResultOnSubmitQuery(String query) {
        List<SearchResult> list = YoutubeIntegratorSingleton
                .searchByQueryString(query);
        if (list == null) {
            handleError(new Exception("No result found"));
            return;
        }
        createSearchResultList(list);
        initRecyclerView(result);
    }

    /**
     *
     * @param result
     */
    private void initRecyclerView(ArrayList<SearchResult> result) {
        soundTrackRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
        soundTrackRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        SoundTrackRecyclerViewAdapter adapter =
                new SoundTrackRecyclerViewAdapter(result, this);
        soundTrackRecyclerView.setAdapter(adapter);
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
        retrieveVideoUrlAsync(videoId, Long.toString(getTimestamp()));
    }

    /**
     *
     * @param url
     */
    private void downloadSoundTrackByUrl(String url) {
        DownloadVolleyResponse volleyResponse = new DownloadVolleyResponse(downloadFilename,
                new WeakReference<OnDownloadCallbackInterface>(this));
        RequestQueue mRequestQueue = Volley.newRequestQueue(getActivity().getApplicationContext(),
                new HurlStack());
        mRequestQueue.add(new InputStreamVolleyRequest(Request.Method.GET, url,
                volleyResponse, volleyResponse, null));
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
    private void playSoundTrack(int position) throws Exception {
        initMediaPlayer();
        SearchResult obj = getTrackByPosition(position);
        String videoId = obj.getId().getVideoId();
        soundTrackTitleTextView.setText(obj.getSnippet().getTitle());

        setOnPlayingStatus(position);
//        testSoundTrackPlay();
        retrieveVideoUrlAsync(videoId, Long.toString(getTimestamp()));
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
                soundTrackProgressbar.setProgress(mediaPlayer.getCurrentPosition());
            }
        });
        observer = new MediaObserver();
        new Thread(observer).start();

    }

    /**
     * 
     * @param videoId
     * @param timestamp
     */
    private void retrieveVideoUrlAsync(final String videoId, String timestamp) {

        // Instantiate the RequestQueue.
        String END_POINT = "https://shrouded-island-4422.herokuapp.com/api/getVideoInfoUrl/" + 
                videoId + "/" + timestamp;
        RequestQueue queue = Volley.newRequestQueue(getActivity());

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, END_POINT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        sendSnackbarMessage("success " + response);
                        String url = response.replaceAll("\"", "");
                        retrieveVideoInfoAsync(videoId, url);
                    }
                }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    handleError(error);
                }
            });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    /**
     *
     * @param videoId
     * @param videoInfoUrl
     */
    private void retrieveVideoInfoAsync(final String videoId, String videoInfoUrl) {
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        Log.e("TAG", videoInfoUrl);
        Log.e("TAG", videoId);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, videoInfoUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            String jsonResult = response.split("=", 2)[1];
                            sendSnackbarMessage("success " + jsonResult);
                            retrieveSoundTrackUrlAsync(videoId, jsonResult);
                        } catch (Exception e) {
                            handleError(e);
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        handleError(error);
                    }
                });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    /**
     *
     * @param jsonResult
     */
    private void retrieveSoundTrackUrlAsync(String videoId, String jsonResult) {
        // Instantiate the RequestQueue.
        String END_POINT = "https://shrouded-island-4422.herokuapp.com/api/getSoundTrackUrl" +
                buildSoundTrackUrlByJSON(videoId, jsonResult);
        Log.e("TAG", END_POINT);
        RequestQueue queue = Volley.newRequestQueue(getActivity());

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, END_POINT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //TODO refactorize it
                        sendSnackbarMessage("success " + response);
                        try {
                            if (soundTrackStatus.isPlayStatus()) {
                                startMediaPlayer(response);
                                return;
                            }
                            if (soundTrackStatus.isDownloadStatus()) {
                                downloadSoundTrackByUrl(response);
                            }
                        } catch (Exception e) {
                            handleError(e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                handleError(error);
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
    /**
     *
     * @param dataString
     * @return
     */
    private String buildSoundTrackUrlByJSON(String videoId, String dataString) {
        try {
            JSONObject reader = new JSONObject(dataString);
            String tsCreate = reader.getString("ts_create");
            String r = reader.getString("r");
            String h2 = reader.getString("h2");
            return "/" + videoId + "/" + tsCreate + "/" + r + "/" + h2;
        } catch (JSONException e) {
            handleError(e);
        }
        return null;
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
    private void startMediaPlayer(String url) throws Exception {
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
        Log.e("TAG", "hey playing" + isPlaying);
        if (searchMenuItem != null) {
            searchMenuItem.setVisible(!isPlaying);
        }

        Resources resources = getActivity().getResources();
        int color = resources.getColor(isPlaying ?
                R.color.md_amber_400 : R.color.md_blue_grey_800);
        ActionBar actionbar = ((AppCompatActivity) getActivity())
                .getSupportActionBar();
        try {
            actionbar.setBackgroundDrawable(new ColorDrawable(color));
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
                if (soundTrackStatus.isIdleStatus()) {
                    return;
                }
                soundTrackStatus.setPlayStatus();
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
    }
    /**
     *
     */
    private void setOnIdleStatus() {
        soundTrackStatus.setCurrentPosition(-1);
        soundTrackRecyclerView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pausePlayingBoxButtonId:
                mp.pause();
                pauseButton.setVisibility(View.GONE);
                playButton.setVisibility(View.VISIBLE);
                break;
            case R.id.playPlayingBoxButtonId:
                mp.start();
                pauseButton.setVisibility(View.VISIBLE);
                playButton.setVisibility(View.GONE);
                break;
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
            return;
        }
        if (statusEnum == DownloadStatusEnum.OK) {
            swipeContainerLayout.setRefreshing(false);
            soundTrackStatus.setIdleStatus();
            soundTrackRecyclerView.getAdapter().notifyDataSetChanged();
            createSnackBarByBackgroundColor("Hey you got your song!", getActivity()
                    .getResources().getColor(R.color.md_amber_400));
        }
    }

    /***
     * TODO move out
     */
    private class MediaObserver implements Runnable {
        private AtomicBoolean stop = new AtomicBoolean(false);

        public void stop() {
            stop.set(true);
        }

        @Override
        public void run() {
            while (!stop.get()) {
                if (mp.isPlaying()) {
                    try {
                        float progress = ((float) mp.getCurrentPosition() / mp.getDuration()) * 100;
                        soundTrackProgressbar.setProgress((int) progress);
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        handleError(e);
                    }
                }
            }
            setOnPlayingActionbar(false);
            setOnPlayingStatus(-1);
        }
    }

}