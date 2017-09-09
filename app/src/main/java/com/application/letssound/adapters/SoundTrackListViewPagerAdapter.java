package com.application.letssound.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.application.letssound.fragments.MostPlayedFragment;
import com.application.letssound.fragments.PlaylistFragment;
import com.application.letssound.fragments.SearchListFragment;

/**
 * Created by davide-syn on 8/9/17.
 */

public class SoundTrackListViewPagerAdapter extends FragmentStatePagerAdapter {
    private static final int NUM_PAGER = 3;

    public SoundTrackListViewPagerAdapter(FragmentManager supportFragmentManager) {
        super(supportFragmentManager);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new SearchListFragment();
            case 1:
                return new PlaylistFragment();
            case 2:
                return new MostPlayedFragment();
        }
        return null;
    }

    @Override
    public String getPageTitle(int position) {
        switch (position) {
            case 0:
                return "SEARCH";
            case 1:
                return "LAST SEARCH";
            case 2:
                return "PLAYLIST";
        }
        return "";
    }

    @Override
    public int getCount() {
        return NUM_PAGER;
    }
}
