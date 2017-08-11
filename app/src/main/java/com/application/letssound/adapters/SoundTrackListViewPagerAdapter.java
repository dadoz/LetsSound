package com.application.letssound.adapters;

import android.content.res.AssetManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.application.letssound.fragments.LatestPlayFragment;
import com.application.letssound.fragments.SearchListFragment;
import com.application.letssound.utils.Utils;

import java.lang.ref.WeakReference;

/**
 * Created by davide-syn on 8/9/17.
 */

public class SoundTrackListViewPagerAdapter extends FragmentStatePagerAdapter {
    private final WeakReference<AssetManager> assetManager;
    private final int NUM_PAGER = 2;

    public SoundTrackListViewPagerAdapter(FragmentManager supportFragmentManager, AssetManager assetManager) {
        super(supportFragmentManager);
        this.assetManager = new WeakReference<>(assetManager);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new SearchListFragment();
            case 1:
                return new LatestPlayFragment();
        }
        return null;
    }

    @Override
    public String getPageTitle(int position) {
        switch (position) {
            case 0:
                return Utils.getCalligraphySpannable("SEARCH", assetManager.get());
            case 1:
                return Utils.getCalligraphySpannable("LATEST PLAY", assetManager.get());
        }
        return "";
    }

    @Override
    public int getCount() {
        return NUM_PAGER;
    }
}
