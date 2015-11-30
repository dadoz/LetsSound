package com.example.davide.letssound.fragments;

/**
 * Created by davide on 26/11/15.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.davide.letssound.R;
import com.example.davide.letssound.singleton.YoutubeIntegratorSingleton;
import com.google.api.services.youtube.model.SearchResult;

import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class HistoryListFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private View mRootView;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static HistoryListFragment newInstance(int sectionNumber) {
        HistoryListFragment fragment = new HistoryListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public HistoryListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_history_list_layout, container, false);
        initView();
        return mRootView;
    }

    /**
     *
     */
    public void initView() {

    }


}