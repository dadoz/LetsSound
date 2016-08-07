package com.application.letssound.fragments;

/**
 * Created by davide on 26/11/15.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.application.letssound.R;

import butterknife.ButterKnife;

/**
 * A placeholder fragment containing a simple view.
 */
public class HistoryListFragment extends Fragment {
    private static final String ARG_SECTION_NUMBER = "section_number";

    /**
     *
     */
    public HistoryListFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static HistoryListFragment getInstace(int sectionNumber) {
        HistoryListFragment fragment = new HistoryListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.fragment_history_list_layout, container, false);
        ButterKnife.bind(this, mainView);
        return mainView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        initView();
    }

    /**
     *
     */
    public void initView() {

    }


}