package com.example.davide.letssound.managers;

import com.example.davide.letssound.auth.AuthCustom;

import java.util.ArrayList;

import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by davide on 15/07/16.
 */
public class RetrofitManager {

    private static final String BASE_URL = "https://www.googleapis.com/youtube/v3/";
    private final YoutubeService service;
    private static final String YOUTUBE_PART = "snippet";
    private static RetrofitManager instance;

    /**
     * constructor
     */
    public RetrofitManager() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .build();

        service = retrofit.create(YoutubeService.class);
    }

    public static RetrofitManager getInstance() {
        return instance == null ?
                instance = new RetrofitManager() : instance;
    }

    /**
     *
     * @return
     */
    public Observable<ArrayList<Object>> searchList(String query) {
        return service.searchList(query, YOUTUBE_PART);
    }

    /**
     * interface
     */
    public interface YoutubeService {
        //part=snippet
        @GET("search?key=" + AuthCustom.API_KEY)
        Observable<ArrayList<Object>> searchList(@Query("q") String query, @Query("part") String part);
    }
}
