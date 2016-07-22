package com.example.davide.letssound.helpers;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.*;
import android.net.wifi.WifiManager;
import android.os.PowerManager;
import android.util.Log;

import com.example.davide.letssound.services.MediaService;

import java.io.IOException;

public class LocalPlayback implements AudioManager.OnAudioFocusChangeListener,
        OnCompletionListener, OnErrorListener, OnPreparedListener, MediaPlayer.OnSeekCompleteListener {

    private static final String TAG = "LocalPlayback";
    private final MediaService service;
    private final AudioManager audioManager;
    private final WifiManager.WifiLock wifiLock;
    private MediaPlayer mp;
//    private MediaService.MediaSessionCallback callback;

    public LocalPlayback(MediaService service) {
        this.service = service;
        this.audioManager = (AudioManager) service.getSystemService(Context.AUDIO_SERVICE);
        // Create the Wifi lock (this does not acquire the lock, this just creates it)
        this.wifiLock = ((WifiManager) service.getSystemService(Context.WIFI_SERVICE))
                .createWifiLock(WifiManager.WIFI_MODE_FULL, "uAmp_lock");
        createMediaPlayer();
    }

    /**
     * 
     */
    public void onPlay(String url) throws IOException {
        mp.setDataSource(url);
        mp.prepareAsync();
    }
    
    @Override
    public void onAudioFocusChange(int i) {
        
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {

    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {

    }

    @Override
    public void onSeekComplete(MediaPlayer mediaPlayer) {

    }

    /**
     * TODO mmmmmm is this the right place
     * Makes sure the media player exists and has been reset. This will create
     * the media player if needed, or reset the existing media player if one
     * already exists.
     */
    private void createMediaPlayer() {
        if (mp != null) {
            mp.reset();
            return;
        }

        mp = new MediaPlayer();
        mp.setWakeMode(service.getApplicationContext(),
                PowerManager.PARTIAL_WAKE_LOCK);
        mp.setOnPreparedListener(this);
        mp.setOnCompletionListener(this);
        mp.setOnErrorListener(this);
        mp.setOnSeekCompleteListener(this);
    }

//    public void setCallback(MediaService.MediaSessionCallback callback) {
//        this.callback = callback;
//    }
}
