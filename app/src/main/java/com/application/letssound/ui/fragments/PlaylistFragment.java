package com.application.letssound.ui.fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.application.letssound.R;
import com.application.letssound.adapters.SoundTrackLatestPlayRecyclerViewAdapter;
import com.application.letssound.adapters.interfaces.LatestPlayAdapterCallbacks;
import com.application.letssound.application.LetssoundApplication;
import com.application.letssound.managers.HistoryManager;
import com.application.letssound.models.SoundTrack;
import com.application.letssound.ui.SoundTrackPlayerActivity;
import com.application.letssound.utils.Utils;
import com.lib.lmn.davide.soundtrackdownloaderlibrary.manager.FileStorageManager;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.Disposable;

/**
 * Created by davide-syn on 8/9/17.
 */
public class PlaylistFragment extends BaseFragment implements SoundTrackLatestPlayRecyclerViewAdapter.OnItemClickListenerInterface, LatestPlayAdapterCallbacks {
    private Unbinder unbinder;

    @BindView(R.id.latestPlayRecyclerViewId)
    RecyclerView latestPlayRecyclerView;
    @BindView(R.id.emptyResultHistoryLayoutId)
    View emptyResultHistoryLayoutId;
    private HistoryManager historyManager;
    private Disposable subjectDisposable;
    ArrayList<SoundTrack> soundTracksList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_playlist_layout, container, false);
        unbinder = ButterKnife.bind(this, view);
        //set history manager
        historyManager = HistoryManager.getInstance(getActivity());

        onInitView();
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        if (subjectDisposable != null)
            subjectDisposable.dispose();
    }

    /**
     * init view
     */
    public void onInitView() {
        //init most played playlist
        soundTracksList = Utils.iteratorToList(historyManager.getSoundTrackIterator());

        //subject to handle searched item
        subjectDisposable = historyManager.getSearchedItemSubject().subscribe(this::initLatestPlayedView);

        //init latest played view
        initLatestPlayedView();
    }

    /**
     *
     */
    public void initLatestPlayedView(SoundTrack soundTrack) {
        soundTracksList.add(soundTrack);
        initLatestPlayedView();
    }

    /**
     *
     */
    public void initLatestPlayedView() {
        //init recycler view
        initLatestPlayedRecyclerView(soundTracksList);
        //update ui
        updateUI(soundTracksList.size() == 0);

    }

    /**
     *
     * @param list
     */
    private void initLatestPlayedRecyclerView(ArrayList<SoundTrack> list) {
        SoundTrackLatestPlayRecyclerViewAdapter adapter =
                new SoundTrackLatestPlayRecyclerViewAdapter(list, this, this, getActivity());
        latestPlayRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        latestPlayRecyclerView.setAdapter(adapter);
    }

    /**
     *
     * @param isEmptyList
     */
    private void updateUI(boolean isEmptyList) {
        emptyResultHistoryLayoutId.setVisibility(isEmptyList ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onItemClick(SoundTrack soundTrack) {
        Bundle bundle = Utils.buildFilePlayBundle(FileStorageManager.Companion.getFullPath(getActivity(),
                soundTrack.getId().getVideoId()),
                soundTrack.getSnippet().getThumbnails().getMedium().getUrl(), soundTrack.getSnippet().getTitle());
        ((LetssoundApplication) getActivity().getApplication()).getMediaControllerInstance()
                .getTransportControls().playFromSearch("CACHED_FILE", bundle);

        //start activity
        Intent intent = new Intent(getActivity(), SoundTrackPlayerActivity.class);
        intent.putExtras(bundle);
        if (getActivity() != null)
            getActivity().startActivity(intent);
    }

    @Override
    public void onItemDismissCallback(String videoId, int position) {
        //add dialog
        new AlertDialog.Builder(getContext())
                .setTitle("Delete Sound Track")
                .setMessage("Sure to delete?")
                .setPositiveButton("DELETE", (dialog, which) -> {
                    ((SoundTrackLatestPlayRecyclerViewAdapter) latestPlayRecyclerView.getAdapter()).removeItem(position);
                    historyManager.removeFromHistory(videoId);
                    if (videoId != null)
                        new FileStorageManager(getActivity(), null).deleteFileOnCache(videoId);
                })
                .setNegativeButton("Clear", (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }

    @Override
    public void onPermissionGrantedCb(String permission) {
    }
}
