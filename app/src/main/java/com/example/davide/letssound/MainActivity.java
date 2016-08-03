package com.example.davide.letssound;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import com.example.davide.letssound.application.LetssoundApplication;
import com.example.davide.letssound.fragments.SearchListFragment;
import icepick.State;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity {
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
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
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
        ((LetssoundApplication) getApplication()).doUnbindService(); //TODO this callback destroy service
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

}
