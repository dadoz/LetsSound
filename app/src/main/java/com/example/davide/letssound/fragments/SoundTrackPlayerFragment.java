package com.example.davide.letssound.fragments;

import android.app.Activity;
import android.app.Application;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.davide.letssound.MainActivity;
import com.example.davide.letssound.R;
import com.example.davide.letssound.application.LetssoundApplication;
import com.example.davide.letssound.managers.MusicPlayerManager;
import com.example.davide.letssound.managers.VolleyMediaArtManager;
import com.example.davide.letssound.services.MediaService;
import com.example.davide.letssound.utils.Utils;
import com.example.davide.letssound.views.CircularNetworkImageView;
import com.example.davide.letssound.views.CircularSeekBar;

import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A placeholder fragment containing a simple view.
 */
public class SoundTrackPlayerFragment extends Fragment implements View.OnClickListener, CircularSeekBar.OnCircularSeekBarChangeListener {
    private static final String TAG = "SoundTrackPlayerFragment";
    private Uri mediaArtUri;
    private String title;
    @Bind(R.id.playerSoundTrackSeekbarId)
    CircularSeekBar playerSoundTrackSeekbar;
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
    private MusicPlayerManager musicPlayerManager;
    private MediaService service;

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

        service = ((LetssoundApplication) getActivity().getApplication()).getMediaService();

        volleyMediaArtManager = VolleyMediaArtManager
                .getInstance(new WeakReference<>(getActivity().getApplicationContext()), null);

        musicPlayerManager = MusicPlayerManager.getInstance(new WeakReference<Activity>(getActivity()),
                new View[] {playerPlayButton, playerPauseButton}, null);
        musicPlayerManager.initMediaController();
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
        playerSoundTrackSeekbar.setOnSeekBarChangeListener(this);
//        playerSoundTrackSeekbar.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                return true;
//            }
//        });
        updateProgressOnSeekBar();
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
                break;
        }
    }

    @Override
    public void onProgressChanged(CircularSeekBar circularSeekBar, int progress, boolean fromUser) {
        if (progress > circularSeekBar.getProgress()) {
            updateProgressOnSeekBar();
        }
    }

    /**
     *
     */
    private void updateProgressOnSeekBar() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (playerSoundTrackSeekbar.getProgress() == 100) {
                    return;
                }

                int current = Utils.getCurrentPosition( service.getMediaPlayerCurrentPosition(),
                        service.getMediaPlayerDuration());
                if (playerSoundTrackSeekbar.getProgress() == 0) {
                    current++;
                }
                playerSoundTrackSeekbar.setProgress(current);
            }
        }, 4000);

    }

    @Override
    public void onStopTrackingTouch(CircularSeekBar seekBar) {

    }

    @Override
    public void onStartTrackingTouch(CircularSeekBar seekBar) {

    }
}