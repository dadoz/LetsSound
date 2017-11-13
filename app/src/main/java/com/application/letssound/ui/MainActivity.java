package com.application.letssound.ui;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.application.letssound.R;
import com.application.letssound.adapters.SoundTrackListViewPagerAdapter;
import com.application.letssound.application.LetssoundApplication;
import com.application.letssound.managers.contentProvider.SoundTrackSuggestionProvider;
import com.application.letssound.ui.fragments.BaseFragment;
import com.application.letssound.ui.fragments.SearchListFragment;
import com.application.letssound.utils.Utils;
import com.lib.lmn.davide.soundtrackdownloaderlibrary.manager.FileStorageManager;

import icepick.State;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MainActivity extends AppCompatActivity {
    @State
    public String currentFragmentTag = SearchListFragment.SEARCH_LIST_FRAG_TAG;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_layout);
        onInitView();
    }
    @Override
    protected void onNewIntent(Intent intent) {
        saveSuggestionQueries(intent);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onStart() {
        super.onStart();
        ((LetssoundApplication) getApplication()).doBindService();
    }

    /**
     * base activity
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem searchMenuItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) MenuItemCompat.getActionView(searchMenuItem);

        //get media search manager
        SearchManager mediaSearchManager = (SearchManager) getSystemService(Activity.SEARCH_SERVICE);
        searchView.setSearchableInfo(mediaSearchManager.getSearchableInfo(getComponentName()));
        return true;
    }

    /**
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:
                this.startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     */
    private void onInitView() {
        initActionBar();
        // Instantiate a ViewPager and a PagerAdapter.
        ViewPager viewPager = (ViewPager) findViewById(R.id.soundTrackListViewPagerId);
        viewPager.setAdapter(new SoundTrackListViewPagerAdapter(getSupportFragmentManager()));
        //set tab layout
        TabLayout tabLayout = ((TabLayout) findViewById(R.id.tabLayoutId));
        tabLayout.setupWithViewPager(viewPager);
        Utils.setCustomViewOnTabLayout(tabLayout, viewPager, this);
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

    /**
     * save suggestion
     */
    private void saveSuggestionQueries(Intent intent) {
        if (intent != null && Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this,
                    SoundTrackSuggestionProvider.AUTHORITY, SoundTrackSuggestionProvider.MODE);
            suggestions.saveRecentQuery(query, null);
            searchView.setQuery(query, true);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (FileStorageManager.Companion.getREQUEST() == requestCode) {
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                forwardPermissionToFragment(WRITE_EXTERNAL_STORAGE);
            } else {
                Snackbar.make(getWindow().getDecorView(), "hey permission not granted", Snackbar.LENGTH_SHORT).show();
            }
        }
    }

    private void forwardPermissionToFragment(String permission) {
        ViewPager viewPager = findViewById(R.id.soundTrackListViewPagerId);
        FragmentStatePagerAdapter adapter = (FragmentStatePagerAdapter) viewPager.getAdapter();
        BaseFragment fragment = (BaseFragment) adapter.instantiateItem(viewPager, 0); //search list frag
        fragment.onPermissionGrantedCb(permission);
    }

}
