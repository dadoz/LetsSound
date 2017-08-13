package com.application.letssound.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.application.letssound.R;
import com.application.letssound.SoundTrackPlayerActivity;
import com.application.letssound.adapters.LatestPlayAdapterCallbacks;
import com.application.letssound.adapters.LatestPlayItemTouchHelper;
import com.application.letssound.adapters.SoundTrackLatestPlayRecyclerViewAdapter;
import com.application.letssound.application.LetssoundApplication;
import com.application.letssound.managers.HistoryManager;
import com.application.letssound.models.SoundTrack;
import com.application.letssound.utils.Utils;
import com.lib.lmn.davide.soundtrackdownloaderlibrary.manager.FileStorageManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.support.v7.widget.helper.ItemTouchHelper.DOWN;
import static android.support.v7.widget.helper.ItemTouchHelper.LEFT;
import static android.support.v7.widget.helper.ItemTouchHelper.UP;

/**
 * Created by davide-syn on 8/9/17.
 */
public class LatestPlayFragment extends Fragment implements SoundTrackLatestPlayRecyclerViewAdapter.OnItemClickListenerInterface, LatestPlayAdapterCallbacks {
    private Unbinder unbinder;

    @BindView(R.id.latestPlayRecyclerViewId)
    RecyclerView latestPlayRecyclerView;
    @BindView(R.id.emptyResultHistoryLayoutId)
    View emptyResultHistoryLayoutId;
    @BindView(R.id.historyTimeTextId)
    TextView historyTimeText;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_latest_played_layout, container, false);
        unbinder = ButterKnife.bind(this, view);
        onInitView();
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    /**
     * init view
     */
    public void onInitView() {
        ArrayList<SoundTrack> list = Utils.iteratorToList(HistoryManager.getInstance(getContext()).getSoundTrackIterator());
        SoundTrackLatestPlayRecyclerViewAdapter adapter = new SoundTrackLatestPlayRecyclerViewAdapter(list, this, this, getContext());
        latestPlayRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        latestPlayRecyclerView.setAdapter(adapter);

        ItemTouchHelper touchHelper = new LatestPlayItemTouchHelper(new LatestPlayItemTouchHelper.ItemTouchSimpleCallbacks(UP | DOWN, LEFT),
                latestPlayRecyclerView.getAdapter());
        touchHelper.attachToRecyclerView(latestPlayRecyclerView);

        historyTimeText.setText(new SimpleDateFormat("dd MMMM", Locale.ITALY).format(new Date()));
        updateUI(list.size() == 0);
    }

    /**
     *
     * @param isEmptyList
     */
    private void updateUI(boolean isEmptyList) {
        emptyResultHistoryLayoutId.setVisibility(isEmptyList ? View.VISIBLE : View.GONE);
        historyTimeText.setVisibility(!isEmptyList ? View.VISIBLE : View.GONE);
    }

    //TODO mv in a presenter maybe
    @Override
    public void onItemClick(SoundTrack soundTrack) {
        Bundle bundle = Utils.buildFilePlayBundle(FileStorageManager.Companion.getFullPath(getContext(),
                soundTrack.getId().getVideoId()),
                soundTrack.getSnippet().getThumbnails().getMedium().getUrl(), soundTrack.getSnippet().getTitle());
        ((LetssoundApplication) getActivity().getApplication()).getMediaControllerInstance()
                .getTransportControls().playFromSearch("CACHED_FILE", bundle);

        //start activity
        Intent intent = new Intent(getContext(), SoundTrackPlayerActivity.class);
//        intent.putExtras(bundle);
        if (getActivity() != null)
            getActivity().startActivity(intent);
    }

    @Override
    public void onItemDismissCallback(String videoId) {
        HistoryManager.getInstance(getContext()).removeFromHistory(videoId);
        new FileStorageManager(getContext(), null).deleteFileOnCache(videoId);
    }
}
