package com.example.davide.letssound.fragments;

/**
 * Created by davide on 26/11/15.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.davide.letssound.R;
import com.example.davide.letssound.singleton.YoutubeIntegratorSingleton;
import com.google.api.services.youtube.model.SearchResult;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * A placeholder fragment containing a simple view.
 */
public class SearchListFragment extends Fragment {
    @Bind(R.id.searchResultListViewId)
    ListView searchResultListView;

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
    public static SearchListFragment newInstance(int sectionNumber) {
        SearchListFragment fragment = new SearchListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public SearchListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_search_list_layout, container, false);
        ButterKnife.bind(this, mRootView);
        initView();
        return mRootView;
    }

    /**
     *
     */
    public void initView() {
        (mRootView
                .findViewById(R.id.searchSubmitButtonId))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String query = ((EditText) mRootView
                                .findViewById(R.id.searchInputId)).getText().toString();
                        List<SearchResult> result = YoutubeIntegratorSingleton
                                .searchByQueryString(query);

                        if (result == null) {
                            return;
                        }

                        //set result
                        ArrayList<String> stringResult = new ArrayList<>();
                        ListIterator litr = result.listIterator();
                        while(litr.hasNext()) {
                            SearchResult element = (SearchResult) litr.next();
                            stringResult.add(element.getSnippet().getTitle());
                        }

                        searchResultListView.setAdapter(new ArrayAdapter<>(getActivity(),
                                android.R.layout.simple_list_item_1, android.R.id.text1, stringResult));

                    }
                });

    }


}