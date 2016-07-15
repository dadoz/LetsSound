package com.example.davide.letssound.helpers;

import android.os.AsyncTask;
import android.os.Handler;

import com.example.davide.letssound.helpers.YoutubeConnector;
import com.example.davide.letssound.managers.SearchManagerInterface;
import com.google.api.services.youtube.model.SearchResult;

import java.util.List;

/**
 * Created by davide on 13/07/16.
 */
public class SearchManager implements SearchManagerInterface {
    @Override
    public Object onSearchSync() {
        return null;
    }

    @Override
    public void onSearchAsync() {

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
