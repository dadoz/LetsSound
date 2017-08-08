package com.application.letssound.fragments;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatSeekBar;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.application.letssound.R;
import com.application.letssound.application.LetssoundApplication;
import com.application.letssound.managers.MusicPlayerManager;
import com.application.letssound.managers.VolleyMediaArtManager;
import com.application.letssound.services.MediaService;
import com.application.letssound.utils.Utils;
import com.application.letssound.views.CircularNetworkImageView;

import java.lang.ref.WeakReference;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * A placeholder fragment containing a simple view.
 */
public class SoundTrackPlayerFragment extends Fragment implements View.OnClickListener,
        AppCompatSeekBar.OnSeekBarChangeListener {
    private static final String TAG = "SoundTrackPlayerFragment";
    private Uri mediaArtUri;
    private String title;
    @Bind(R.id.playerSoundTrackSeekbarId)
    AppCompatSeekBar playerSoundTrackSeekbar;
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
    @Bind(R.id.playerTrackRetryButtonId)
    View playerTrackRetryButton;

    private int current;
    private VolleyMediaArtManager volleyMediaArtManager;
    private MusicPlayerManager musicPlayerManager;
    private MediaService service;
    private static final int REFRESH_TIMESTAMP = 5000;
    private BroadcastReceiver errorBroadcastReceiver;
    private View mainView;

    public SoundTrackPlayerFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.fragment_sound_track_player_layout, container, false);
        ButterKnife.bind(this, mainView);

        service = ((LetssoundApplication) getActivity().getApplication()).getMediaService();

        volleyMediaArtManager = VolleyMediaArtManager
                .getInstance(new WeakReference<>(getActivity().getApplicationContext()), null);

        musicPlayerManager = MusicPlayerManager.getInstance(new WeakReference<Activity>(getActivity()),
                new View[] {playerPlayButton, playerPauseButton}, null);
        musicPlayerManager.initMediaController();
        return mainView;
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
//        Log.e(TAG, "uri " + mediaArtUri.toString());
//        playerMediaArtImageView
//                .setImageUrl(mediaArtUri.toString(), volleyMediaArtManager.getImageLoader());
        playerTrackTitleText.setText(title);
        playerPlayButton.setOnClickListener(this);
        playerPauseButton.setOnClickListener(this);
        playerRepeatButton.setOnClickListener(this);
        playerTrackRetryButton.setOnClickListener(this);
        initSeekbar();
    }

    @Override
    public void onStart() {
        super.onStart();
        registerErrorMediaplayerBroadcast();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (errorBroadcastReceiver != null) {
            getActivity().unregisterReceiver(errorBroadcastReceiver);
            errorBroadcastReceiver = null;
        }
    }
    /**
     *
     */
    private void initSeekbar() {
        //get position from mediaSession
        playerSoundTrackSeekbar.setOnSeekBarChangeListener(this);
        playerSoundTrackSeekbar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });
        updateProgressOnSeekBar(REFRESH_TIMESTAMP + 2000);
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
                updateProgressOnSeekBar(REFRESH_TIMESTAMP);
                break;
            case R.id.playerPauseButtonId:
                musicPlayerManager.pause();
                break;
            case R.id.playerRepeatButtonId:
                Bundle bundle = getArguments();
                musicPlayerManager.repeatOne(bundle);
                playerSoundTrackSeekbar.setProgress(0);
                updateProgressOnSeekBar(REFRESH_TIMESTAMP);
                break;
            case R.id.playerTrackRetryButtonId:
                musicPlayerManager.repeatOne(getArguments());
                playerSoundTrackSeekbar.setProgress(0);
                updateProgressOnSeekBar(REFRESH_TIMESTAMP);
                playerTrackRetryButton.setVisibility(View.GONE);
                break;
        }
    }

    /**
     * FIXME - leaking Runnable
     */
    private synchronized void updateProgressOnSeekBar(int timestamp) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (playerSoundTrackSeekbar.getProgress() == 100) {
                    return;
                }

                current = Utils.getCurrentPosition(service.getMediaPlayerCurrentPosition(),
                        service.getMediaPlayerDuration());
                if (playerSoundTrackSeekbar.getProgress() == 0) {
                    current++;
                }
                if (current > playerSoundTrackSeekbar.getProgress()) {
                    playerSoundTrackSeekbar.setProgress(current);
                }
            }
        }, timestamp);

    }


    /**
     *
     */
    private void registerErrorMediaplayerBroadcast() {
        errorBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                handleErrorOnPlayingSoundTrack();
            }
        };
        getActivity().registerReceiver(errorBroadcastReceiver,
                new IntentFilter(MediaService.ERROR_MEDIAPLAYER_BROADCAST));
    }

    /**
     *
     */
    private void handleErrorOnPlayingSoundTrack() {
        playerTrackRetryButton.setVisibility(View.VISIBLE);
        Utils.createSnackBarByBackgroundColor(mainView, "Snap! Error on playing song.",
                ContextCompat.getColor(getActivity().getApplicationContext(), R.color.md_red_400));
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (progress > seekBar.getProgress()) {
            updateProgressOnSeekBar(REFRESH_TIMESTAMP);
        }

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}