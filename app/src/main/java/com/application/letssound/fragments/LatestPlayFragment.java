package com.application.letssound.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.application.letssound.R;
import com.application.letssound.adapters.SoundTrackHistoryRecyclerViewAdapter;
import com.application.letssound.managers.HistoryManager;
import com.application.letssound.models.SoundTrack;
import com.application.letssound.utils.Utils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by davide-syn on 8/9/17.
 */
public class LatestPlayFragment extends Fragment implements SoundTrackHistoryRecyclerViewAdapter.OnItemClickListenerInterface {
    private Unbinder unbinder;

    @BindView(R.id.historyListViewId)
    RecyclerView historyListView;
    @BindView(R.id.emptyResultHistoryLayoutId)
    View emptyResultHistoryLayoutId;


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
        SoundTrackHistoryRecyclerViewAdapter adapter = new SoundTrackHistoryRecyclerViewAdapter(list, this, getContext());
        historyListView.setLayoutManager(new LinearLayoutManager(getContext()));
        historyListView.setAdapter(adapter);
        updateUI(list.size() != 0);
    }

    /**
     *
     * @param isEMptyList
     */
    private void updateUI(boolean isEMptyList) {
        emptyResultHistoryLayoutId.setVisibility(isEMptyList ? View.VISIBLE : View.GONE);
    }

    //TODO mv in a presenter maybe
    @Override
    public void onItemClick(SoundTrack soundTrack) {
        Log.e(getClass().getName(), "hye");
    }

}
