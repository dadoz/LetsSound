package com.example.davide.letssound.fragments;

/**
 * Created by davide on 26/11/15.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.davide.letssound.R;
import butterknife.ButterKnife;

/**
 * A placeholder fragment containing a simple view.
 */
public class SoundTrackPlayerFragment extends Fragment {
    private View mRootView;
    public SoundTrackPlayerFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_sound_track_player_layout, container, false);
        ButterKnife.bind(this, mRootView);
        initView();
        return mRootView;
    }

    /**
     *
     */
    public void initView() {
//        setHasOptionsMenu(true);
    }

}