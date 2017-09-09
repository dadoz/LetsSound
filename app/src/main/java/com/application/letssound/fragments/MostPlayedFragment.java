package com.application.letssound.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.application.letssound.R;
import com.application.letssound.SoundTrackPlayerActivity;
import com.application.letssound.adapters.LatestPlayAdapterCallbacks;
import com.application.letssound.adapters.SoundTrackLatestPlayRecyclerViewAdapter;
import com.application.letssound.application.LetssoundApplication;
import com.application.letssound.managers.HistoryManager;
import com.application.letssound.models.SoundTrack;
import com.application.letssound.utils.Utils;
import com.application.letssound.views.MostPlayedPlaylistView;
import com.lib.lmn.davide.soundtrackdownloaderlibrary.manager.FileStorageManager;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.Disposable;

/**
 * Created by davide-syn on 8/9/17.
 */
public class MostPlayedFragment extends Fragment implements SoundTrackLatestPlayRecyclerViewAdapter.OnItemClickListenerInterface, LatestPlayAdapterCallbacks {
    private Unbinder unbinder;

    @BindView(R.id.mostPlayedPlaylistContainerId)
    MostPlayedPlaylistView mostPlayedPlaylistContainer;
    private HistoryManager historyManager;
    private Disposable subjectDisposable;
    private ArrayList<SoundTrack> soundTracksList;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_most_played_layout, container, false);
        unbinder = ButterKnife.bind(this, view);
        //set history manager
        historyManager = HistoryManager.getInstance(getContext());

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
        subjectDisposable = historyManager.getSearchedItemSubject().subscribe(this::initMostPlayedList);

        //init most played playlist
        mostPlayedPlaylistContainer.initMostPlayedView(soundTracksList);
    }

    public void initMostPlayedList(SoundTrack soundTrack) {
        soundTracksList.add(soundTrack);
        mostPlayedPlaylistContainer.initMostPlayedView(soundTracksList);
    }

    /**
     *
     * @param isEmptyList
     */
    private void updateUI(boolean isEmptyList) {
    }

    @Override
    public void onItemClick(SoundTrack soundTrack) {
        Bundle bundle = Utils.buildFilePlayBundle(FileStorageManager.Companion.getFullPath(getContext(),
                soundTrack.getId().getVideoId()),
                soundTrack.getSnippet().getThumbnails().getMedium().getUrl(), soundTrack.getSnippet().getTitle());
        ((LetssoundApplication) getActivity().getApplication()).getMediaControllerInstance()
                .getTransportControls().playFromSearch("CACHED_FILE", bundle);

        //start activity
        Intent intent = new Intent(getContext(), SoundTrackPlayerActivity.class);
        intent.putExtras(bundle);
        if (getActivity() != null)
            getActivity().startActivity(intent);
    }

    @Override
    public void onItemDismissCallback(String videoId) {
        historyManager.removeFromHistory(videoId);
        if (videoId != null)
            new FileStorageManager(getContext(), null).deleteFileOnCache(videoId);
    }
}
