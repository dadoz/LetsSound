package com.application.letssound.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.application.letssound.R;
import com.application.letssound.SoundTrackPlayerActivity;
import com.application.letssound.adapters.LatestPlayAdapterCallbacks;
import com.application.letssound.adapters.LatestPlayItemTouchHelper;
import com.application.letssound.adapters.SoundTrackLatestPlayRecyclerViewAdapter;
import com.application.letssound.adapters.SoundTrackMostPlayRecyclerViewAdapter;
import com.application.letssound.application.LetssoundApplication;
import com.application.letssound.managers.HistoryManager;
import com.application.letssound.models.SoundTrack;
import com.application.letssound.utils.Utils;
import com.lib.lmn.davide.soundtrackdownloaderlibrary.manager.FileStorageManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.Disposable;

import static android.support.v7.widget.helper.ItemTouchHelper.DOWN;
import static android.support.v7.widget.helper.ItemTouchHelper.LEFT;
import static android.support.v7.widget.helper.ItemTouchHelper.UP;

/**
 * Created by davide-syn on 8/9/17.
 */
public class PlaylistFragment extends Fragment implements SoundTrackLatestPlayRecyclerViewAdapter.OnItemClickListenerInterface, LatestPlayAdapterCallbacks {
    private Unbinder unbinder;

    @BindView(R.id.latestPlayRecyclerViewId)
    RecyclerView latestPlayRecyclerView;
    @BindView(R.id.mostPlayRecyclerViewId)
    RecyclerView mostPlayRecyclerView;
    @BindView(R.id.emptyResultHistoryLayoutId)
    View emptyResultHistoryLayoutId;
    @BindView(R.id.mostPlayedPlayAllButtonId)
    View mostPlayedPlayAllButton;
    private HistoryManager historyManager;
    private Disposable subjectDisposable;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_playlist_layout, container, false);
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
        //subject to handle searched item
        subjectDisposable = historyManager.getSearchedItemSubject().subscribe(soundTrack -> initLatestPlayedView());

        //init latest played view
        initLatestPlayedView();

        //init most played playlist
        initMostPlayedView();
    }

    private void initMostPlayedView() {
        ArrayList<SoundTrack> list = Utils.iteratorToList(historyManager.getSoundTrackIterator());
        List<SoundTrack> mostPlayedList = list.subList(0, 3);

        initMostPlayedRecyclerView(mostPlayedList);
    }

    private void initMostPlayedRecyclerView(List<SoundTrack> list) {
        SoundTrackMostPlayRecyclerViewAdapter adapter = new SoundTrackMostPlayRecyclerViewAdapter(list);
        mostPlayRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        mostPlayRecyclerView.setAdapter(adapter);
        mostPlayedPlayAllButton.setOnClickListener(view -> Snackbar.make(view, "play all", Snackbar.LENGTH_SHORT).show());
    }

    /**
     *
     */
    public void initLatestPlayedView() {
        ArrayList<SoundTrack> list = Utils.iteratorToList(historyManager.getSoundTrackIterator());
        //init recycler view
        initLatestPlayedRecyclerView(list);
        //update ui
        updateUI(list.size() == 0);
    }

    /**
     *
     * @param list
     */
    private void initLatestPlayedRecyclerView(ArrayList<SoundTrack> list) {
        SoundTrackLatestPlayRecyclerViewAdapter adapter = new SoundTrackLatestPlayRecyclerViewAdapter(list, this, this, getContext());
        latestPlayRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        latestPlayRecyclerView.setAdapter(adapter);
        ItemTouchHelper touchHelper = new LatestPlayItemTouchHelper(new LatestPlayItemTouchHelper.ItemTouchSimpleCallbacks(UP | DOWN, LEFT),
                latestPlayRecyclerView.getAdapter());
        touchHelper.attachToRecyclerView(latestPlayRecyclerView);
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
