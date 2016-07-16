package com.example.davide.letssound.managers;

import android.util.Log;

import com.example.davide.letssound.helpers.ObservableHelper;
import com.example.davide.letssound.models.SoundTrack;
import com.google.api.client.util.DateTime;
import com.google.api.services.youtube.model.ResourceId;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.SearchResultSnippet;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Date;

import rx.Subscription;

/**
 * Created by davide on 13/07/16.
 */
public class TrackSearchManager implements SearchManagerInterface,
        ObservableHelper.ObservableHelperInterface {
    private static TrackSearchManager instance;
    private static RetrofitManager retrofitManager;
    private static ObservableHelper observableHelper;
    private static WeakReference<TrackSearchManagerInterface> listener;

    /**
     *
     * @return
     */
    public static TrackSearchManager getInstance(WeakReference<TrackSearchManagerInterface> lst) {
        if (instance == null) {
            instance = new TrackSearchManager();
        }
        listener = lst;
        retrofitManager = RetrofitManager.getInstance();
        observableHelper = ObservableHelper
                .getInstance(new WeakReference<ObservableHelper.ObservableHelperInterface>(instance));
        return instance;
    }

    @Override
    public Object onSearchSync(String query) {
        return null;
    }

    @Override
    public Subscription onSearchAsync(String query) {
        return observableHelper.initObservable(retrofitManager.searchList(query));
    }

    @Override
    public void onObservableSuccess(ArrayList<Object> list, String requestType) {
        Log.e("TAG", "success results  " + list.size());
        listener.get().onTrackSearchSuccess(list);
    }

    @Override
    public void onObservableEmpty() {
        Log.e("TAG", "empty");
        listener.get().onTrackSearchError("empty fields");
    }

    @Override
    public void onObservableError(String requestType, String error) {
        Log.e("TAG", "hey" + error);
        listener.get().onTrackSearchError(error);
    }

    public interface TrackSearchManagerInterface {
        void onTrackSearchSuccess(Object list);
        void onTrackSearchError(String error);
    }

    /**
     * @deprecated
     * @param query
     */
/*    public void setAsyncResultOnSubmitQuery(final String query) {
        String[] queryArray = {query};
        new AsyncTask<String, Boolean, Boolean>() {
            @Override
            protected Boolean doInBackground(String... params) {
                setResultOnSubmitQuery(params[0]);
                return true;
            }
        }.execute(queryArray);

    }

    /**
     * @deprecated
     * @param query
     */
/*    private void setResultOnSubmitQuery(String query) {
        List<SearchResult> list = youtubeConnector
                .search(query);
        if (list == null) {
            handleError(new Exception("No result found"));
            return;
        }
        createSearchResultList(list);
        initRecyclerView(result);
    }

    /**
     * @deprecated
     */
/*    private void fillList() {
        ArrayList<SearchResult> result = new ArrayList<>();
        result.add(createSearchResultObj("hey"));
        result.add(createSearchResultObj("res"));
        result.add(createSearchResultObj("bla"));
        result.add(createSearchResultObj("hey"));
        result.add(createSearchResultObj("res"));
        result.add(createSearchResultObj("bla"));
        result.add(createSearchResultObj("hey"));
        result.add(createSearchResultObj("res"));
        result.add(createSearchResultObj("bla"));
//        initRecyclerView(result);
    }

    /**
     *
     * @param title
     * @return
     */
/*    private SearchResult createSearchResultObj(String title) {
        SearchResult obj = new SearchResult();
        SearchResultSnippet snippet = new SearchResultSnippet();
        snippet.setTitle(title);
        snippet.setPublishedAt(new DateTime(new Date()));
        obj.setKind("John Bleam");
        obj.setId(new ResourceId().setVideoId("blaaaaaaurl bla claaaa"));
        obj.setSnippet(snippet);
        return obj;
    }
    */

}
