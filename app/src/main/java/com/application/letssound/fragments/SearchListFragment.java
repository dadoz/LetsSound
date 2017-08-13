package com.application.letssound.fragments;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.application.letssound.R;
import com.application.letssound.SoundTrackPlayerActivity;
import com.application.letssound.adapters.SoundTrackRecyclerViewAdapter;
import com.application.letssound.application.LetssoundApplication;
import com.application.letssound.helpers.SoundTrackStatus;
import com.application.letssound.managers.HistoryManager;
import com.application.letssound.managers.MediaSearchManager;
import com.application.letssound.managers.MusicPlayerManager;
import com.application.letssound.models.SoundTrack;
import com.application.letssound.observer.SoundTrackSearchObserver;
import com.application.letssound.utils.Utils;
import com.lib.lmn.davide.soundtrackdownloaderlibrary.modules.SoundTrackDownloaderModule;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.FileInputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import icepick.Icepick;
import icepick.State;

import static com.application.letssound.adapters.SoundTrackRecyclerViewAdapter.OnItemClickListenerInterface;

/**
 * A placeholder fragment containing a simple view.
 */
public class SearchListFragment extends Fragment implements
        SwipeRefreshLayout.OnRefreshListener,
        SearchView.OnQueryTextListener, MediaPlayer.OnErrorListener,
        MediaPlayer.OnPreparedListener, OnItemClickListenerInterface,
        MediaSearchManager.TrackSearchManagerInterface, MusicPlayerManager.OnMusicPlayerCallback,
        View.OnFocusChangeListener, SoundTrackDownloaderModule.OnSoundTrackRetrievesCallbacks {
    public static String SEARCH_LIST_FRAG_TAG = "SEARCH_LIST_FRAG_TAG";
    @State
    ArrayList<SoundTrack> soundTrackList;
    @BindView(R.id.trackRecyclerViewId)
    RecyclerView soundTrackRecyclerView;
    @BindView(R.id.searchNoItemLayoutId)
    View searchNoItemLayout;
    @BindView(R.id.searchNoItemImageId)
    View searchNoItemImage;
    @BindView(R.id.emptyResultProgressBarId)
    ProgressBar emptyResultProgressBar;
    private SoundTrackStatus soundTrackStatus;
    private MenuItem searchMenuItem;
    private View mainView;
    private MediaSearchManager mediaSearchManager;
    private MusicPlayerManager musicPlayerManager;
    private HistoryManager historyManager;
    private Unbinder unbinder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);
        initComponents();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.fragment_search_list_layout, container, false);
        unbinder = ButterKnife.bind(this, mainView);
        return mainView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        initView(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        mediaSearchManager = null;
    }

    /**
     * init components
     */
    private void initComponents() {
        soundTrackStatus = SoundTrackStatus.getInstance();
//        downloaderHelper = DownloaderHelper.getInstance(new WeakReference<Activity>(getActivity()),
//                new WeakReference<OnDownloadHelperResultInterface>(this));
        mediaSearchManager = MediaSearchManager.getInstance(this);
        musicPlayerManager = MusicPlayerManager.getInstance(getActivity(), null, this);
        historyManager = HistoryManager.getInstance(getActivity().getApplicationContext());
    }

    /**
     *
     * @param savedInstanceState
     */
    public void initView(Bundle savedInstanceState) {
        updateUI(false);
        setHasOptionsMenu(true);
        initRecyclerView(savedInstanceState != null ? soundTrackList : new ArrayList<SoundTrack>());
    }


    @Override
    public void onRefresh() {
    }

    @Override
    public void onItemClick(SoundTrack soundTrack) {
        Snackbar snackbar = Snackbar.make(getView(), "loading song", Snackbar.LENGTH_SHORT);
        snackbar.getView().setBackgroundColor(ContextCompat.getColor(getContext(), R.color.md_amber_400));
        snackbar.show();
        musicPlayerManager.playSoundTrack(getContext(),
                this,
                soundTrack.getId().getVideoId(),
                soundTrack.getSnippet().getThumbnails().getMedium().getUrl(),
                soundTrack.getSnippet().getTitle());
        historyManager.saveOnHistory(soundTrack);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
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
        mediaSearchManager.onSearchAsync(query);
        updateUI(true);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
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
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchMenuItem);
        searchView.setOnQueryTextFocusChangeListener(this);

        //get media search manager
        SearchManager mediaSearchManager = (SearchManager) getActivity()
                .getSystemService(Activity.SEARCH_SERVICE);
        searchView.setSearchableInfo(mediaSearchManager
                .getSearchableInfo(getActivity().getComponentName()));
        searchView.setOnQueryTextListener(this);

    }

    /**
     *
     * @param result
     */
    private void initRecyclerView(final ArrayList<SoundTrack> result) {
        //if you want you can save all result on history or in db
//        historyManager.saveOnHistory(result);
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
     * @param result
     */
    private void setResultOnSavedInstance(ArrayList<SoundTrack> result) {
        this.soundTrackList = result;
    }

    @Override
    public void onTrackSearchSuccess(Object list) {
        ArrayList<SoundTrack> tmp = (ArrayList<SoundTrack>) list;
        setResultOnSavedInstance(tmp);
        updateRecyclerViewData(tmp);
        updateUI(false);
    }

    @Override
    public void onTrackSearchError(String error) {
        setResultOnSavedInstance(new ArrayList<SoundTrack>());
        updateRecyclerViewData(new ArrayList<SoundTrack>());
        Utils.createSnackBarByBackgroundColor(mainView, error, ContextCompat
                .getColor(getContext(), R.color.md_red_400));
        updateUI(false);
    }

    @Override
    public void onPlayMediaCallback(Bundle bundle) {
    }

    @Override
    public void onFocusChange(View view, boolean isVisible) {
    }

    @Override
    public void onSoundTrackRetrieveSuccess(@NotNull String filePath, @NotNull FileInputStream fileInputStream) {
        Bundle bundle = Utils.buildFilePlayBundle(filePath, "", "Sound Track Title");
        ((LetssoundApplication) getActivity().getApplication()).getMediaControllerInstance()
                .getTransportControls().playFromSearch("CACHED_FILE", bundle);

        //update ui
        updateUI(false);

        //start activity
        Intent intent = new Intent(getContext(), SoundTrackPlayerActivity.class);
//        intent.putExtras(bundle);
        if (getActivity() != null)
            getActivity().startActivity(intent);

    }

    @Override
    public void onSoundTrackRetrieveError(@Nullable String message) {
        Snackbar snackbar = Utils.buildErrorSnackbar(getView(), message);
        if (snackbar != null)
            snackbar.show();
    }

    /**
     * update ui
     */
    private void updateUI(final boolean isLoading) {
        //update ui
        if (getActivity() != null)
            getActivity().runOnUiThread(() -> {
                emptyResultProgressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
                soundTrackRecyclerView.setVisibility(!isLoading ? View.VISIBLE : View.GONE);
                searchNoItemImage.setVisibility(!isLoading ? View.VISIBLE : View.GONE);
            });
    }


}