package com.application.letssound.presenters;

import android.graphics.PorterDuff;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.application.letssound.R;

/**
 * Created by davide on 14/07/16.
 */
public class MusicPlayerPresenter {

    /**
     * TODO move on presenter
     * init progress bar
     */
/*    private void initProgressBar() {
        soundTrackPlayingBoxLayoutHeader = getActivity()
                .findViewById(R.id.soundTrackPlayingBoxLayoutId);
        soundTrackPlayingBoxLayoutHeader.setVisibility(View.GONE);
        soundTrackSeekbar = (SeekBar) soundTrackPlayingBoxLayoutHeader
                .findViewById(R.id.soundTrackSeekbarId);
        durationTimeActionbarTextView = (TextView) soundTrackPlayingBoxLayoutHeader
                .findViewById(R.id.durationTimeActionbarTextId);
        currentTimeActionbarTextView = (TextView) soundTrackPlayingBoxLayoutHeader
                .findViewById(R.id.currentTimeActionbarTextId);
        soundTrackTitleTextView = (TextView) soundTrackPlayingBoxLayoutHeader
                .findViewById(R.id.soundTrackTitleTextId);
        pauseButton = getActivity()
                .findViewById(R.id.pausePlayingBoxButtonId);
        playButton = getActivity()
                .findViewById(R.id.playPlayingBoxButtonId);
        shareTextView = soundTrackPlayingBoxLayoutHeader
                .findViewById(R.id.shareTextId);
        downloadTextView = soundTrackPlayingBoxLayoutHeader
                .findViewById(R.id.downloadTextId);

        shareTextView.setOnClickListener(this);
        downloadTextView.setOnClickListener(this);
        pauseButton.setOnClickListener(this);
        playButton.setOnClickListener(this);
        initPlayerUI();

        int color = getActivity().getResources().getColor(R.color.md_violet_custom_0);
        soundTrackSeekbar.getProgressDrawable().setColorFilter(color, PorterDuff.Mode.SRC_IN);
        soundTrackSeekbar.getThumb().setColorFilter(color, PorterDuff.Mode.SRC_IN);
    }
    /**
     *
     * @param isPlaying
     */
/*    private void setOnPlayingActionbar(final boolean isPlaying) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //header status
                setTopActionbar(isPlaying);
                getActivity().findViewById(R.id.slidingTabsId).setVisibility(isPlaying ?
                        View.GONE : View.VISIBLE);
                soundTrackPlayingBoxLayoutHeader.setVisibility(isPlaying ?
                        View.VISIBLE : View.GONE);
            }
        });
    }
    /**
     *
     */
/*    private void onPlayingStatusPrepareUI() {
        swipeContainerLayout.setRefreshing(false);
        setOnPlayingActionbar(true);
    }
*/
}
