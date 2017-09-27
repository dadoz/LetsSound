package com.application.letssound.ui.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatSeekBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.application.letssound.R;
import com.application.letssound.application.LetssoundApplication;
import com.application.letssound.managers.MusicPlayerManager;
import com.application.letssound.helpers.VolleyMediaArtHelper;
import com.application.letssound.services.MediaService;
import com.application.letssound.utils.Utils;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * A placeholder fragment containing a simple view.
 */
public class SoundTrackPlayerFragment extends Fragment implements View.OnClickListener {
    @BindView(R.id.playerSoundTrackSeekbarId)
    AppCompatSeekBar playerSoundTrackSeekbar;
    @BindView(R.id.playerMediaArtImageViewId)
    NetworkImageView playerMediaArtImageView;
    @BindView(R.id.playerTrackTitleTextId)
    TextView playerTrackTitleText;
    @BindView(R.id.playerPlayButtonId)
    View playerPlayButton;
    @BindView(R.id.playerPauseButtonId)
    View playerPauseButton;
    @BindView(R.id.playerRepeatButtonId)
    View playerRepeatButton;
    @BindView(R.id.playerTrackRetryButtonId)
    View playerTrackRetryButton;

    private String title;
    private Uri mediaArtUri;
    private VolleyMediaArtHelper volleyMediaArtManager;
    private MusicPlayerManager musicPlayerManager;
    private MediaService service;
    private Unbinder unbinder;

    //defining error broadcast receiver
    private BroadcastReceiver errorBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            playerTrackRetryButton.setVisibility(View.VISIBLE);
            Utils.createSnackBarByBackgroundColor(getView(), "Snap! Error on playing song.",
                    ContextCompat.getColor(getActivity().getApplicationContext(), R.color.md_red_400));
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        service = ((LetssoundApplication) getActivity().getApplication()).getMediaService();
        volleyMediaArtManager = VolleyMediaArtHelper
                .getInstance(new WeakReference<>(getActivity().getApplicationContext()), null);
        registerErrorMediaplayerBroadcast();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.fragment_sound_track_player_layout, container, false);
        unbinder = ButterKnife.bind(this, mainView);
        return mainView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        initFromBundle();
        initView();
    }

    @Override
    public void onStop() {
        super.onStop();
        unregisterErrorMediaplayerBroadcast();
    }

    /**
     *
     */
    public void initView() {
        musicPlayerManager = MusicPlayerManager.getInstance(getActivity(),
                new View[] {playerPlayButton, playerPauseButton}, null);
        musicPlayerManager.initMediaController();

        if (mediaArtUri != null) {
            playerMediaArtImageView.setImageUrl(mediaArtUri.toString(), volleyMediaArtManager.getImageLoader());
        }
        playerTrackTitleText.setText(title);
        playerPlayButton.setOnClickListener(this);
        playerPauseButton.setOnClickListener(this);
        playerRepeatButton.setOnClickListener(this);
        playerTrackRetryButton.setOnClickListener(this);

//        mediaPlayer = (MediaController.MediaPlayerControl)
//                service.mediaController.getMediaController();
//
//        SeekBar seeker = playerSoundTrackSeekbar;
//        seeker.setOnSeekBarChangeListener(mSeekListener);
//        playerSoundTrackSeekbar.setMax(1000);
    }


    /**
     *
     */
    private void registerErrorMediaplayerBroadcast() {
        getActivity().registerReceiver(errorBroadcastReceiver,
                new IntentFilter(MediaService.ERROR_MEDIAPLAYER_BROADCAST));
    }
    /**
     *
     */
    private void unregisterErrorMediaplayerBroadcast() {
        if (errorBroadcastReceiver != null) {
            getActivity().unregisterReceiver(errorBroadcastReceiver);
            errorBroadcastReceiver = null;
        }
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
                musicPlayerManager.play();
                break;
            case R.id.playerPauseButtonId:
                musicPlayerManager.pause();
                break;
            case R.id.playerRepeatButtonId:
                Bundle bundle = getArguments();
                musicPlayerManager.repeatOne(bundle);
                playerSoundTrackSeekbar.setProgress(0);
                break;
            case R.id.playerTrackRetryButtonId:
                musicPlayerManager.repeatOne(getArguments());
                playerSoundTrackSeekbar.setProgress(0);
                playerTrackRetryButton.setVisibility(View.GONE);
                break;
        }
    }

}