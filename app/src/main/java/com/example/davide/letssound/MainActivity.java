package com.example.davide.letssound;

import java.lang.ref.WeakReference;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.davide.letssound.adapters.SectionsPagerAdapter;
import com.example.davide.letssound.services.MediaService;

public class MainActivity extends AppCompatActivity {
    SectionsPagerAdapter mSectionsPagerAdapter;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        doBindService();
        initView();
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
        bindService(new Intent(MainActivity.this, MediaService.class),
                serviceConnection, Context.BIND_AUTO_CREATE);
    }

    /**
     * undbind service
     */
    private void doUnbindService() {
        unbindService(serviceConnection);
    }

    private MediaService boundService;
    /**
     *
     */
    public ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            boundService = ((MediaService.MediaBinder) iBinder).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            boundService = null;
        }
    };
}
