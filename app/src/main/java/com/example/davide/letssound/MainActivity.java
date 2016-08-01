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
import android.os.RemoteException;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.davide.letssound.application.LetssoundApplication;
import com.example.davide.letssound.fragments.SearchListFragment;
import com.example.davide.letssound.managers.MusicPlayerManager;
import com.example.davide.letssound.services.MediaService;

import icepick.State;

public class MainActivity extends AppCompatActivity {
//    SectionsPagerAdapter mSectionsPagerAdapter;
//    ViewPager viewPager;
    private String TAG = "MainActivity";
    @State
    public String currentFragmentTag = SearchListFragment.SEARCH_LIST_FRAG_TAG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        ((LetssoundApplication) getApplication()).doBindService();
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
        ((LetssoundApplication) getApplication()).doUnbindService();
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
        initFragment();
//        initViewPager();
    }

    /**
     *
     */
    private void initFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.mainFragmentContainerId, getSuitableFragment(), currentFragmentTag)
                .commit();
    }

    /**
     *
     * @return
     */
    public Fragment getSuitableFragment() {
        Fragment frag;
        return (frag = getSupportFragmentManager().findFragmentByTag(this.currentFragmentTag)) == null ?
                new SearchListFragment() : frag;
    }

    /**
     * TODO move in a base class
     */
    private void initActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarId);
        if (toolbar != null) {
            toolbar.setTitle(R.string.search_title);
            setSupportActionBar(toolbar);
        }
    }

//    /**
//     * TODO move in a base class
//     */
//    private void initViewPager() {
//        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(),
//                new WeakReference<Activity>(this));
//        viewPager = (ViewPager) findViewById(R.id.pagerId);
//        if (viewPager != null) {
//            viewPager.setAdapter(mSectionsPagerAdapter);
//        }
//
//        // Give the TabLayout the ViewPager
//        TabLayout tabLayout = (TabLayout) findViewById(R.id.slidingTabsId);
//        if (tabLayout != null) {
//            tabLayout.setupWithViewPager(viewPager);
//        }
//    }



}
