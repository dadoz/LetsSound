package com.application.letssound.adapters.observer;

/**
 * Created by davide on 13/07/16.
 */

import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/***
 * TODO move out
 */
public class MediaObserver implements Runnable {
    private AtomicBoolean stop = new AtomicBoolean(false);

    public void stop() {
        stop.set(true);
    }

    /**
     *
     * @param millisec
     * @return
     */
    public String parseMillisecToString(long millisec) {
        return String.format(Locale.getDefault(), "%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(millisec),
                TimeUnit.MILLISECONDS.toSeconds(millisec) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisec)));
    }

    @Override
    public void run() {

    }

    /**
     *
     */
//    private void updateView() {
//        getActivity().runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                currentTimeActionbarTextView.setText(parseMillisecToString(mp.getCurrentPosition()));
//                durationTimeActionbarTextView.setText(parseMillisecToString(mp.getDuration()));
//            }
//        });
//    }

//    @Override
//    public void run() {
//        while (!stop.get()) {
//            if (mp.isPlaying()) {
//                try {
//                    float progress = ((float) mp.getCurrentPosition() / mp.getDuration()) * 100;
//                    updateView();
//                    soundTrackSeekbar.setProgress((int) progress);
//                    Thread.sleep(100);
//                } catch (InterruptedException e) {
//                    handleError(e);
//                }
//            }
//        }
//        setOnPlayingActionbar(false);
//        setOnPlayingStatus(SoundTrackStatus.INVALID_POSITION);
//    }
}