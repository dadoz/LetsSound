package com.application.letssound.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.application.letssound.fragments.SearchListFragment;

/**
 * Created by davide-syn on 8/9/17.
 */

public class ActivityUtils {
    public static Fragment getCurrentFragment(FragmentManager supportFragmentManager, String currentFragmentTag) {
        Fragment frag;
        return (frag = supportFragmentManager.findFragmentByTag(currentFragmentTag)) == null ?
                new SearchListFragment() : frag;

    }
}
