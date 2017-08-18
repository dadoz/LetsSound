package com.application.letssound.views;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import com.application.letssound.R;
import com.application.letssound.adapters.SoundTrackMostPlayRecyclerViewAdapter;
import com.application.letssound.models.SoundTrack;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by davide-syn on 8/17/17.
 */
public class MostPlayedPlaylistView extends CardView {
    RecyclerView mostPlayRecyclerView;
    View mostPlayedPlayAllButton;

    public MostPlayedPlaylistView(Context context) {
        super(context);
        onInit();
    }

    public MostPlayedPlaylistView(Context context, AttributeSet attrs) {
        super(context, attrs);
        onInit();
    }

    public MostPlayedPlaylistView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        onInit();
    }

    void onInit() {
        inflate(getContext(), R.layout.most_played_playlist_layout, this);
        mostPlayRecyclerView = (RecyclerView) findViewById(R.id.mostPlayRecyclerViewId);
        mostPlayedPlayAllButton = findViewById(R.id.mostPlayedPlayAllButtonId);
    }

    public void initMostPlayedView(ArrayList<SoundTrack> list) {
        List<SoundTrack> mostPlayedList = list.subList(0, 3);

        initMostPlayedRecyclerView(mostPlayedList);
    }

    private void initMostPlayedRecyclerView(List<SoundTrack> list) {
        SoundTrackMostPlayRecyclerViewAdapter adapter = new SoundTrackMostPlayRecyclerViewAdapter(list);
        mostPlayRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        mostPlayRecyclerView.setAdapter(adapter);
        mostPlayedPlayAllButton.setOnClickListener(view -> Snackbar.make(view, "play all", Snackbar.LENGTH_SHORT).show());
    }

}
