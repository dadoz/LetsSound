package com.application.letssound.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.application.letssound.R;
import com.application.letssound.views.EmptyDataView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by davide-syn on 8/9/17.
 */
public class SoundtrackPlayListFragment extends Fragment {
    private Unbinder unbinder;

    @BindView(R.id.emptySoundTrackPlaylistViewId)
    EmptyDataView emptySoundTrackPlaylistView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_soundtrack_playlist_layout, container, false);
        unbinder = ButterKnife.bind(this, view);
        onInitView();
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (unbinder != null)
            unbinder.unbind();
    }

    /**
     * init view
     */
    public void onInitView() {
        //custom empty view
        emptySoundTrackPlaylistView.setIconRes(R.drawable.ic_disc_48dp);
        emptySoundTrackPlaylistView.setMessage(getString(R.string.no_playlist_available));
    }
}
