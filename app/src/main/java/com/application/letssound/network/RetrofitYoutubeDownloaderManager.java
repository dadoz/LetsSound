package com.application.letssound.network;

import com.application.letssound.helpers.GsonConverterHelper;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by davide on 15/07/16.
 */
public class RetrofitYoutubeDownloaderManager {

    private final static String FORMAT_TYPE = "JSON";
    private final static String YOUTUBE_BASE_PATH = "https://www.youtube.com/watch?v=";
    private final static String BASE_URL =  "http://www.youtubeinmp3.com/";
    private final YoutubeDownloaderService service;
    private static RetrofitYoutubeDownloaderManager instance;

    /**
     * constructor
     */
    public RetrofitYoutubeDownloaderManager() {
        service = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterHelper.getGsonConverter())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build()
                .create(YoutubeDownloaderService.class);
    }

    public static RetrofitYoutubeDownloaderManager getInstance() {
        return instance == null ?
                instance = new RetrofitYoutubeDownloaderManager() : instance;
    }

    /**
     *
     * @return
     */
    public Observable<String> fetchUrlByVideoId(String videoId) {
        return service.fetchUrlByVideoId(FORMAT_TYPE, YOUTUBE_BASE_PATH + videoId);
    }

    /**
     * interface
     */
    public interface YoutubeDownloaderService {
        @GET("fetch")
        Observable<String> fetchUrlByVideoId(@Query("format") String format, @Query("video") String video);
    }
}
