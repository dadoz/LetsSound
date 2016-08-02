package com.example.davide.letssound.fragments;

/**
 * Created by davide on 26/11/15.
 */

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.davide.letssound.R;
import com.example.davide.letssound.application.LetssoundApplication;
import com.example.davide.letssound.managers.MusicPlayerControllerManager;
import com.example.davide.letssound.managers.MusicPlayerManager;
import com.example.davide.letssound.managers.VolleyMediaArtManager;
import com.example.davide.letssound.services.MediaService;
import com.example.davide.letssound.views.CircularNetworkImageView;

import java.lang.ref.WeakReference;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.angrybyte.circularslider.CircularSlider;

/**
 * A placeholder fragment containing a simple view.
 */
public class SoundTrackPlayerFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "SoundTrackPlayerFragment";
    private Uri mediaArtUri;
    private String title;
    @Bind(R.id.playerSoundTrackSeekbarId)
    CircularSlider playerSoundTrackSeekbar;
    @Bind(R.id.playerMediaArtImageViewId)
    CircularNetworkImageView playerMediaArtImageView;
    @Bind(R.id.playerTrackTitleTextId)
    TextView playerTrackTitleText;
    @Bind(R.id.playerPlayButtonId)
    View playerPlayButton;
    @Bind(R.id.playerPauseButtonId)
    View playerPauseButton;
    @Bind(R.id.playerRepeatButtonId)
    View playerRepeatButton;

    private VolleyMediaArtManager volleyMediaArtManager;
    private MusicPlayerControllerManager musicPlayerControllerManager;

    public SoundTrackPlayerFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mRootView = inflater.inflate(R.layout.fragment_sound_track_player_layout, container, false);
        ButterKnife.bind(this, mRootView);
        volleyMediaArtManager = VolleyMediaArtManager
                .getInstance(new WeakReference<>(getActivity().getApplicationContext()), null);

        musicPlayerControllerManager = MusicPlayerControllerManager.getInstance(new WeakReference<Activity>(getActivity()),
                new View[] {playerPlayButton, playerPauseButton});
        musicPlayerControllerManager.initMediaController();
        return mRootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        initFromBundle();
        initView();
    }

    /**
     *
     */
    public void initView() {
        Log.e(TAG, "uri " + mediaArtUri.toString());
//        playerMediaArtImageView
//                .setImageUrl(mediaArtUri.toString(), volleyMediaArtManager.getImageLoader());
        playerTrackTitleText.setText(title);
        playerPlayButton.setOnClickListener(this);
        playerPauseButton.setOnClickListener(this);
        playerRepeatButton.setOnClickListener(this);
        initSeekbar();
    }

    /**
     *
     */
    private void initSeekbar() {
        //get position from mediaSession
        playerSoundTrackSeekbar.setPosition(0);
    }

    /**
     *
     */
    private void initFromBundle() {
        if (getArguments() != null) {
            title = getArguments().getString(MediaService.PARAM_TRACK_TITLE);
            mediaArtUri = getArguments().getParcelable(MediaService.PARAM_TRACK_THUMBNAIL);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.playerPlayButtonId:
                musicPlayerControllerManager.play();
                break;
            case R.id.playerPauseButtonId:
                musicPlayerControllerManager.pause();
                break;
            case R.id.playerRepeatButtonId:
                Bundle bundle = getArguments();
                musicPlayerControllerManager.repeatOne(bundle);
                break;
        }
    }
}