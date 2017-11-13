package com.application.letssound.ui.fragments;


import android.support.v4.app.Fragment;

/**
 * Created by davide-syn on 11/13/17.
 */

public abstract class BaseFragment extends Fragment {
    abstract public void onPermissionGrantedCb(String permission);
}
