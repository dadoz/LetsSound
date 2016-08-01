package com.example.davide.letssound;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.davide.letssound.fragments.SoundTrackPlayerFragment;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by davide on 08/02/16.
 */
public class SoundTrackPlayerActivity extends AppCompatActivity {
    @Bind(R.id.toolbarId)
    Toolbar toolbar;

    private String TAG = "SOUND_TRACK_PLAYER_FRAG_TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        ButterKnife.bind(this);

        initView();
    }

    private void initActionbar() {
        toolbar.setTitle("Player");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /**
     *
     */
    private void initView() {
        inflateFragment();
        initActionbar();
    }

    /**
     *
     */
    private void inflateFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainerId, getSuitableFragment())
                .commit();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     *
     * @return
     */
    public Fragment getSuitableFragment() {
        Fragment frag = null;
        return (frag = getSupportFragmentManager().findFragmentByTag(TAG)) == null ?
                new SoundTrackPlayerFragment() :
                frag;
    }
}
