package com.example.davide.letssound.fragments;
import android.app.Activity;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
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

import com.example.davide.letssound.MainActivity;
import com.example.davide.letssound.R;
import com.example.davide.letssound.downloader.DownloadVolleyResponse.DownloadStatusEnum;
import com.example.davide.letssound.downloader.helper.DownloaderHelper;
import com.example.davide.letssound.downloader.helper.OnDownloadHelperResultInterface;
import com.example.davide.letssound.helpers.SoundTrackStatus;
import com.example.davide.letssound.adapters.SoundTrackRecyclerViewAdapter;
import com.example.davide.letssound.managers.MediaSearchManager;
import com.example.davide.letssound.managers.MusicPlayerManager;
import com.example.davide.letssound.models.SoundTrack;
import com.example.davide.letssound.services.MediaService;
import com.example.davide.letssound.utils.Utils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import icepick.Icepick;
import icepick.State;

import static com.example.davide.letssound.adapters.SoundTrackRecyclerViewAdapter.*;
import static com.example.davide.letssound.downloader.DownloadVolleyResponse.*;

/**
 * A placeholder fragment containing a simple view.
 */
public class SearchListFragment extends Fragment implements
        View.OnClickListener, OnDownloadHelperResultInterface, SwipeRefreshLayout.OnRefreshListener,
        SearchView.OnQueryTextListener, MediaPlayer.OnErrorListener,
        MediaPlayer.OnPreparedListener, OnItemClickListenerInterface,
        OnDownloadCallbackInterface, MediaSearchManager.TrackSearchManagerInterface, MusicPlayerManager.OnMusicPlayerCallback {
    @Bind(R.id.trackRecyclerViewId)
    RecyclerView soundTrackRecyclerView;
    @Bind(R.id.swipeContainerLayoutId)
    SwipeRefreshLayout swipeContainerLayout;

    private static final String ARG_SECTION_NUMBER = "section_number";
    private SearchView searchView;
    @State
    ArrayList<SoundTrack> trackList = new ArrayList<>();
    private SoundTrackStatus soundTrackStatus;
    private MenuItem searchMenuItem;
    private View mainView;
    private DownloaderHelper downloaderHelper;
    private MediaSearchManager searchManager;
    private MusicPlayerManager musicPlayerManager;
    private MediaService boundService;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static SearchListFragment getInstance(int sectionNumber) {
        SearchListFragment fragment = new SearchListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //init component
        soundTrackStatus = SoundTrackStatus.getInstance();
        downloaderHelper = DownloaderHelper.getInstance(new WeakReference<Activity>(getActivity()),
                new WeakReference<OnDownloadHelperResultInterface>(this));
        searchManager = MediaSearchManager.getInstance(new WeakReference<MediaSearchManager.TrackSearchManagerInterface>(this));

        musicPlayerManager = MusicPlayerManager.getInstance(new WeakReference<Activity>(getActivity()),
                new WeakReference<>(boundService), new WeakReference<MusicPlayerManager.OnMusicPlayerCallback>(this));
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.fragment_search_list_layout, container, false);
        ButterKnife.bind(this, mainView);
        return mainView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        initView(savedInstanceState);
    }

    /**
     *
     * @param savedInstanceState
     */
    public void initView(Bundle savedInstanceState) {
//        initSwipeRefresh();
//        initProgressBar();
        setHasOptionsMenu(true);
        if (savedInstanceState != null) {
            initViewFromSavedInstance(savedInstanceState);
            return;
        }
        initRecyclerView(new ArrayList<SoundTrack>());
    }

    /**
     *
     * @param savedInstanceState
     */
    private void initViewFromSavedInstance(Bundle savedInstanceState) {
        //TODO implement this
//        if (BuildConfig.DEBUG)
        initRecyclerView(trackList);
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
    public void downloadSoundTrackByUrl(String url) {
    }

    @Override
    public void onDownloadCallback(DownloadStatusEnum statusEnum, Exception e) {
        //download callback
    }

    @Override
    public void onItemClick(int position, View v) {
        String videoId = trackList.get(position).getId().getVideoId();
        musicPlayerManager.playSoundTrack(videoId);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.pausePlayingBoxButtonId:
//                mp.pause();
//                setPlayPlayerUI(false);
//                break;
//            case R.id.playPlayingBoxButtonId:
//                mp.start();
//                setPlayPlayerUI(true);
//                break;
            case R.id.downloadTextId:
//                downloadAction();
                break;
            case R.id.shareTextId:
//                shareAction();
                break;
        }
    }

    @Override
    public void handleError(Exception e) {
        swipeContainerLayout.setRefreshing(false);
//        setOnIdleStatus();
        Utils.createSnackBarByBackgroundColor(mainView, e.getMessage(), ContextCompat
                .getColor(getContext(), R.color.md_red_400));
    }

    @Override
    public void handleSuccess(String message) {
        Utils.createSnackBarByBackgroundColor(mainView, message,
                ContextCompat.getColor(getContext(), R.color.md_teal_400));
    }

    @Override
    public void startMediaPlayer(String url) throws Exception {
        //startMediaPlayer(url);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        initSearchOptionMenu(menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
//                stopPlaying();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        searchMenuItem.collapseActionView();
        soundTrackStatus.setIdleStatus();
        searchManager.onSearchAsync(query);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
//        setPlayPlayerUI(true);
//        onPlayingStatusPrepareUI();
        mp.start();
    }


    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        mp.reset();
        return false;
    }

    /**
     *
     * @param menu
     */
    private void initSearchOptionMenu(Menu menu) {
        searchMenuItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) MenuItemCompat.getActionView(searchMenuItem);
        SearchManager searchManager = (SearchManager) getActivity()
                .getSystemService(Activity.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getActivity().getComponentName()));
        searchView.setOnQueryTextListener(this);
    }

    /**
     *
     * @param result
     */
    private void initRecyclerView(final ArrayList<SoundTrack> result) {
        soundTrackRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        soundTrackRecyclerView.setAdapter(new SoundTrackRecyclerViewAdapter(result,
                new WeakReference<OnItemClickListenerInterface >(this)));
    }

    /**
     *
     */
    private void updateRecyclerViewData(ArrayList<SoundTrack> list) {
        SoundTrackRecyclerViewAdapter adapter = ((SoundTrackRecyclerViewAdapter) soundTrackRecyclerView
                .getAdapter());
        adapter.removeAll();
        adapter.addAll(list);
        adapter.notifyDataSetChanged();
    }

    /**
     *
     */
    private void initSwipeRefresh() {
        swipeContainerLayout.setOnRefreshListener(this);
    }

    /**
     *
     * @param result
     */
    private void setResultOnSavedInstance(ArrayList<SoundTrack> result) {
        this.trackList = result;
    }

    /**
     *
     * @param searchResultList
     */
//    private void createSearchResultList(List<SearchResult> searchResultList) {
//        result.clear();
//        result.addAll(searchResultList);
//    }

    /**
     *
     * @return
     */
    public long getTimestamp() {
        return System.currentTimeMillis();
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
//                soundTrackPlayingBoxLayoutHeader.setVisibility(isSelected ?
//                        View.VISIBLE : View.GONE);

            }
        });
    }

    /**
     *
     * @param isPlaying
     */
    private void setTopActionbar(boolean isPlaying) {
        if (searchMenuItem != null) {
            searchMenuItem.setVisible(!isPlaying);
        }

        ActionBar actionbar = ((AppCompatActivity) getActivity())
                .getSupportActionBar();
        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(isPlaying);
            actionbar.setDisplayShowTitleEnabled(!isPlaying);
        }
    }

    @Override
    public void onTrackSearchSuccess(Object list) {
        //TODO fix this
        ArrayList<SoundTrack> tmp = (ArrayList<SoundTrack>) list;
        setResultOnSavedInstance(tmp);
        updateRecyclerViewData(tmp);
    }


    @Override
    public void onTrackSearchError(String error) {
        setResultOnSavedInstance(new ArrayList<SoundTrack>());
        updateRecyclerViewData(new ArrayList<SoundTrack>());
    }


    @Override
    public void onPlayMediaCallback(Bundle bundle) {
        //TODO fix it big leak
        ((MainActivity) getActivity()).playMedia(bundle);
    }

}