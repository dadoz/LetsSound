package com.application.letssound.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.application.letssound.fragments.LatestPlayFragment;
import com.application.letssound.fragments.SearchListFragment;

/**
 * Created by davide-syn on 8/9/17.
 */

public class SoundTrackListViewPagerAdapter extends FragmentStatePagerAdapter {
    private final int NUM_PAGER = 2;

    public SoundTrackListViewPagerAdapter(FragmentManager supportFragmentManager) {
        super(supportFragmentManager);
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
                return "SEARCH";
            case 1:
                return "LATEST PLAY";
        }
        return "";
    }

    @Override
    public int getCount() {
        return NUM_PAGER;
    }
}
