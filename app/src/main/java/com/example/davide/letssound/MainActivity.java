package com.example.davide.letssound;

import java.lang.ref.WeakReference;
import java.util.List;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaMetadata;
import android.media.session.MediaController;
import android.media.session.MediaController.Callback;
import android.media.session.MediaSession;
import android.media.session.PlaybackState;
import android.net.Uri;
import android.os.IBinder;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.davide.letssound.adapters.SectionsPagerAdapter;
import com.example.davide.letssound.managers.MusicPlayerManager;
import com.example.davide.letssound.services.MediaService;

public class MainActivity extends AppCompatActivity {
    SectionsPagerAdapter mSectionsPagerAdapter;
    ViewPager viewPager;
    private String TAG = "AppCompatActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        doBindService();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        doUnbindService();
    }

    /**
     * TODO move in a base class
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * TODO move in a base class
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:
                openSettings();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * TODO move in a base class
     */
    private void openSettings() {
        this.startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
    }

    /**
     * TODO move in a base class
     */
    private void initView() {
        initActionBar();
        initViewPager();
    }

    /**
     * TODO move in a base class
     */
    private void initActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarId);
        if (toolbar != null) {
            toolbar.setTitle(R.string.app_name);
            setSupportActionBar(toolbar);
        }
    }

    /**
     * TODO move in a base class
     */
    private void initViewPager() {
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(),
                new WeakReference<Activity>(this));
        viewPager = (ViewPager) findViewById(R.id.pagerId);
        if (viewPager != null) {
            viewPager.setAdapter(mSectionsPagerAdapter);
        }

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.slidingTabsId);
        if (tabLayout != null) {
            tabLayout.setupWithViewPager(viewPager);
        }
    }


    /**
     * start service and bind it
     */
    private void doBindService() {
        bindService(new Intent(this, MediaService.class),
                serviceConnection, Context.BIND_AUTO_CREATE);
    }

    /**
     * undbind service
     */
    private void doUnbindService() {
        unbindService(serviceConnection);
    }
    public MediaController mediaController;

    public MediaController.Callback mcCallback = new MediaController.Callback () {

        @Override
        public void onSessionEvent(String event, Bundle extras) {
        }

        @Override
        public void onPlaybackStateChanged(PlaybackState state) {
            switch (state.getState()) {
                case PlaybackState.STATE_NONE:
                    break;
                case PlaybackState.STATE_PLAYING:
                    Log.e(TAG, "Hey playing");
                    break;
                case PlaybackState.STATE_PAUSED:
                    break;
                case PlaybackState.STATE_REWINDING:
                    break;
            }
        }

        @Override
        public void onMetadataChanged(MediaMetadata metadata) {
        }

        @Override
        public void onQueueChanged(List<MediaSession.QueueItem> queue) {
        }

        @Override
        public void onAudioInfoChanged(MediaController.PlaybackInfo info) {
        }
    };
    /**
     *
     */
    public ServiceConnection serviceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            mediaController = new MediaController(MainActivity.this,
                    ((MediaService.MediaBinder) iBinder).getMediaSessionToken());
            mediaController.registerCallback(mcCallback);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mediaController = null;
        }
    };

    /**
     * @deprecated
     */
    public void playMedia(Bundle bundle) {
        mediaController.getTransportControls().playFromSearch("", bundle);
    }
}
