package com.example.davide.letssound.managers;

import com.example.davide.letssound.helpers.ObservableHelper;

import rx.Subscription;

/**
 * Created by davide on 13/07/16.
 */
public class TrackSearchManager implements SearchManagerInterface {
    private static TrackSearchManager instance;
    private static RetrofitManager retrofitManager;
    private static ObservableHelper observableHelper;

    /**
     *
     * @return
     */
    public static TrackSearchManager getInstance() {
        retrofitManager = RetrofitManager.getInstance();
        observableHelper = ObservableHelper.getInstance();
        return instance == null ?
                instance = new TrackSearchManager() : instance;
    }

    @Override
    public Object onSearchSync(String query) {
        return null;
    }

    @Override
    public Subscription onSearchAsync(String query) {
        return observableHelper.initObservable(retrofitManager.searchList(query));
    }

    /**
     *
     * @param queryString
     */
/*    private void searchOnYoutubeAsync(final String queryString) {
        final Handler handler = new Handler();
        new Thread(){
            public void run(){
                youtubeConnector = YoutubeConnector.getInstance();
                final List<SearchResult> list = youtubeConnector.search(queryString);
                handler.post(new Runnable(){
                    public void run(){
                        if (list == null) {
                            handleError(new Exception("No result found"));
                            return;
                        }
                        createSearchResultList(list);
                        initRecyclerView(result);
                    }
                });
            }
        }.start();
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
*/

}
