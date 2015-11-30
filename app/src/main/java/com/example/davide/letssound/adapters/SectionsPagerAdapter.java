package com.example.davide.letssound.adapters;

/**
 * Created by davide on 26/11/15.
 */

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.davide.letssound.MainActivity;
import com.example.davide.letssound.R;
import com.example.davide.letssound.fragments.HistoryListFragment;
import com.example.davide.letssound.fragments.SearchListFragment;

import java.lang.ref.WeakReference;
import java.util.Locale;

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    private static final int N_TOTç_PAGES = 2;
    private final WeakReference<Activity> activityRef;

    public SectionsPagerAdapter(FragmentManager fm, WeakReference<Activity> activityRef) {
        super(fm);
        this.activityRef = activityRef;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        switch (position) {
            case 0:
            default:
                return SearchListFragment.newInstance(position);
            case 1:
                return HistoryListFragment.newInstance(position);
        }
    }

    @Override
    public int getCount() {
        return N_TOTç_PAGES;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Locale l = Locale.getDefault();
        switch (position) {
            case 0:
                return activityRef.get().getString(R.string.title_section1).toUpperCase(l);
            case 1:
                return activityRef.get().getString(R.string.title_section2).toUpperCase(l);
        }
        return null;
    }

}