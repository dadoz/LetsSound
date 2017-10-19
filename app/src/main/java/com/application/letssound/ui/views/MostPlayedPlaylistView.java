package com.application.letssound.ui.views;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.application.letssound.R;
import com.application.letssound.adapters.SoundTrackMostPlayRecyclerViewAdapter;
import com.application.letssound.models.SoundTrack;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by davide-syn on 8/17/17.
 */
public class MostPlayedPlaylistView extends LinearLayout {
    RecyclerView mostPlayRecyclerView;
    View mostPlayedPlayAllButton;
    private WeakReference<OnPlayAllClikListener> lst;

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
        //set style
        setBackgroundColor(ContextCompat.getColor(getContext(), R.color.md_blue_grey_900));
    }

    public void initMostPlayedView(ArrayList<SoundTrack> list, OnPlayAllClikListener lst) {
        this.lst = new WeakReference<>(lst);
        List<SoundTrack> mostPlayedList = list.isEmpty() ? new ArrayList<>() : list;
        initMostPlayedRecyclerView(mostPlayedList);
    }

    private void initMostPlayedRecyclerView(List<SoundTrack> list) {
        SoundTrackMostPlayRecyclerViewAdapter adapter = new SoundTrackMostPlayRecyclerViewAdapter(list, getContext());
        mostPlayRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        mostPlayRecyclerView.setAdapter(adapter);
        mostPlayedPlayAllButton.setOnClickListener(view -> { lst.get().onPlayAllClick(view); Snackbar.make(view, "play all", Snackbar.LENGTH_SHORT).show(); });
    }

    public interface OnPlayAllClikListener {
        void onPlayAllClick(View view);
    }
}