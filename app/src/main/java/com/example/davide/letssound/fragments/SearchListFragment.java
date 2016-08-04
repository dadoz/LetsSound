package com.example.davide.letssound.fragments;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.NotificationCompat;
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
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;

import com.example.davide.letssound.MainActivity;
import com.example.davide.letssound.R;
import com.example.davide.letssound.SoundTrackPlayerActivity;
import com.example.davide.letssound.adapters.HistoryAdapter;
import com.example.davide.letssound.application.LetssoundApplication;
import com.example.davide.letssound.downloader.helper.DownloaderHelper;
import com.example.davide.letssound.helpers.NotificationHelper;
import com.example.davide.letssound.helpers.SoundTrackStatus;
import com.example.davide.letssound.adapters.SoundTrackRecyclerViewAdapter;
import com.example.davide.letssound.managers.HistoryManager;
import com.example.davide.letssound.managers.MediaSearchManager;
import com.example.davide.letssound.managers.MusicPlayerManager;
import com.example.davide.letssound.models.HistoryResult;
import com.example.davide.letssound.models.SoundTrack;
import com.example.davide.letssound.observer.SoundTrackSearchObserver;
import com.example.davide.letssound.services.MediaService;
import com.example.davide.letssound.utils.Utils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import icepick.Icepick;
import icepick.State;

import static com.example.davide.letssound.adapters.SoundTrackRecyclerViewAdapter.*;

/**
 * A placeholder fragment containing a simple view.
 */
public class SearchListFragment extends Fragment implements
        SwipeRefreshLayout.OnRefreshListener,
        SearchView.OnQueryTextListener, MediaPlayer.OnErrorListener,
        MediaPlayer.OnPreparedListener, OnItemClickListenerInterface,
        MediaSearchManager.TrackSearchManagerInterface, MusicPlayerManager.OnMusicPlayerCallback,
        View.OnFocusChangeListener, AdapterView.OnItemClickListener {
    public static String SEARCH_LIST_FRAG_TAG = "SEARCH_LIST_FRAG_TAG";
    @Bind(R.id.trackRecyclerViewId)
    RecyclerView soundTrackRecyclerView;
    @Bind(R.id.swipeContainerLayoutId)
    SwipeRefreshLayout swipeContainerLayout;
    @Bind(R.id.searchNoItemLayoutId)
    View searchNoItemLayout;

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
    private HistoryManager historyManager;
    private static final String TAG = "SearchListFragment";
    private AutoCompleteTextView autoCompleteTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);
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
//        trackList = null;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initComponents();
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
        historyManager = HistoryManager.getInstance(new WeakReference<>(getActivity().getApplicationContext()));
        initView(savedInstanceState);
    }

    /**
     * init components
     */
    private void initComponents() {
        soundTrackStatus = SoundTrackStatus.getInstance();
//        downloaderHelper = DownloaderHelper.getInstance(new WeakReference<Activity>(getActivity()),
//                new WeakReference<OnDownloadHelperResultInterface>(this));
        searchManager = MediaSearchManager.getInstance(new WeakReference<MediaSearchManager.TrackSearchManagerInterface>(this));

        musicPlayerManager = MusicPlayerManager.getInstance(new WeakReference<Activity>(getActivity()), null,
                new WeakReference<MusicPlayerManager.OnMusicPlayerCallback>(this));
    }

    /**
     *
     * @param savedInstanceState
     */
    public void initView(Bundle savedInstanceState) {
        initSwipeRefresh();
        setHasOptionsMenu(true);
        if (savedInstanceState != null) {
            initViewFromSavedInstance();
            return;
        }

        initRecyclerView(new ArrayList<SoundTrack>());
    }

    /**
     *
     */
    private void initViewFromSavedInstance() {
        initRecyclerView(trackList);
    }

    @Override
    public void onRefresh() {
        swipeContainerLayout.setRefreshing(false);
    }

    @Override
    public void onItemClick(int position, View v) {
        if (trackList == null ||
                trackList.size() < position) {
            Log.e(TAG, "did u forget to initialize list?");
            return;
        }

        SoundTrack soundTrack = trackList.get(position);
        musicPlayerManager.playSoundTrack(soundTrack.getId().getVideoId(),
                soundTrack.getSnippet().getThumbnails().getMedium().getUrl(),
                soundTrack.getSnippet().getTitle());
        historyManager.saveOnHistory(soundTrack);
        setAutocompleteTextViewAdapter();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

//    @Override
//    public void downloadSoundTrackByUrl(String url) {
//    }
//    @Override
//    public void handleError(Exception e) {
//        swipeContainerLayout.setRefreshing(false);
////        setOnIdleStatus();
//        Utils.createSnackBarByBackgroundColor(mainView, e.getMessage(), ContextCompat
//                .getColor(getContext(), R.color.md_red_400));
//    }
//
//    @Override
//    public void handleSuccess(String message) {
//        Utils.createSnackBarByBackgroundColor(mainView, message,
//                ContextCompat.getColor(getContext(), R.color.md_teal_400));
//    }

//    @Override
//    public void startMediaPlayer(String url) throws Exception {
//    }

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
        clearList();
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
        searchView.setOnQueryTextFocusChangeListener(this);
        initAutocompleteTextView();
        SearchManager searchManager = (SearchManager) getActivity()
                .getSystemService(Activity.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getActivity().getComponentName()));
        searchView.setOnQueryTextListener(this);

    }

    /**
     * @TODO move on presenter
     */
    private void initAutocompleteTextView() {
        if (searchView != null) {
            autoCompleteTextView = (AutoCompleteTextView) searchView
                    .findViewById(android.support.v7.appcompat.R.id.search_src_text);

            setAutocompleteTextViewAdapter();
            autoCompleteTextView.setVisibility(View.VISIBLE);
            autoCompleteTextView.setOnItemClickListener(this);
            autoCompleteTextView.setDropDownBackgroundResource(R.color.md_violet_custom_3);
            autoCompleteTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    autoCompleteTextView.showDropDown();
                }
            });
            avoidToAutoCollapseDropdown();
        }

    }

    /**
     * 
     */
    private void avoidToAutoCollapseDropdown() {
        View tmp = searchView.findViewById(autoCompleteTextView.getDropDownAnchor());
        tmp.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (searchView != null &&
                        autoCompleteTextView != null &&
                        !autoCompleteTextView.isPopupShowing() &&
                        !searchView.isIconified()) {
                    autoCompleteTextView.showDropDown();
                }
            }
        });

    }

    /**
     *
     */
    private void setAutocompleteTextViewAdapter() {
        HistoryAdapter historyAdapter = new HistoryAdapter(getActivity().getApplicationContext(),
                R.layout.history_item,
                historyManager.getHistory());
        autoCompleteTextView.setAdapter(historyAdapter);
    }

    /**
     *
     * @param result
     */
    private void initRecyclerView(final ArrayList<SoundTrack> result) {
        SoundTrackRecyclerViewAdapter adapter = new SoundTrackRecyclerViewAdapter(result,
                new WeakReference<OnItemClickListenerInterface>(this),
                new WeakReference<>(getActivity().getApplicationContext()));
        adapter.registerAdapterDataObserver(new SoundTrackSearchObserver(new WeakReference<>(adapter),
                searchNoItemLayout));
        soundTrackRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        soundTrackRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    /**
     *
     */
    private void updateRecyclerViewData(ArrayList<SoundTrack> list) {
        SoundTrackRecyclerViewAdapter adapter = ((SoundTrackRecyclerViewAdapter) soundTrackRecyclerView
                .getAdapter());
        adapter.clearAll();
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
        Utils.createSnackBarByBackgroundColor(mainView, error, ContextCompat
                .getColor(getContext(), R.color.md_red_400));
    }


    @Override
    public void onPlayMediaCallback(Bundle bundle) {
        //TODO fix it big leak
        ((LetssoundApplication) getActivity().getApplication()).playMedia(bundle);
        Intent intent = new Intent(getContext(), SoundTrackPlayerActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    /**
     * @deprecated
     */
    private void clearList() {
        if (trackList != null) {
            trackList.clear();
        }
        ((SoundTrackRecyclerViewAdapter) soundTrackRecyclerView.getAdapter()).clearAll();
    }

    @Override
    public void onFocusChange(View view, boolean isVisible) {
        if (autoCompleteTextView != null &&
                isVisible) {
            autoCompleteTextView.showDropDown();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        HistoryResult HistoryResult = (HistoryResult) adapterView.getAdapter().getItem(i);
        handleItemClick(HistoryResult);
    }

    /**
     *
     * @param HistoryResult
     */
    private void handleItemClick(HistoryResult HistoryResult) {
        SoundTrack soundTrack = historyManager.findSoundTrackByTitle(HistoryResult.getTitle());
        musicPlayerManager.playSoundTrack(soundTrack.getId().getVideoId(),
                soundTrack.getSnippet().getThumbnails().getMedium().getUrl(),
                soundTrack.getSnippet().getTitle());

        if (autoCompleteTextView != null &&
                searchMenuItem != null) {
            searchMenuItem.collapseActionView();
            autoCompleteTextView.setText("");
        }
    }

}