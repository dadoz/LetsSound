package com.example.davide.letssound.managers;

import android.content.Context;

import com.example.davide.letssound.auth.AuthCustom;
import com.example.davide.letssound.helpers.GsonConverterHelper;
import com.example.davide.letssound.interceptors.CacheControlApplicationInterceptor;
import com.example.davide.letssound.models.SoundTrack;

import java.lang.ref.WeakReference;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by davide on 15/07/16.
 */
public class RetrofitManager {

    private static final String BASE_URL = "https://www.googleapis.com/youtube/v3/";
    private final YoutubeService service;
    private static final String YOUTUBE_PART = "snippet";
    private static RetrofitManager instance;
    private WeakReference<Context> contextRef;

    /**
     * constructor
     */
    public RetrofitManager() {
        service = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(getClient())
                .addConverterFactory(GsonConverterHelper.getGsonConverter())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build()
                .create(YoutubeService.class);
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
        return service.searchList(query, YOUTUBE_PART).map(new Func1<ArrayList<SoundTrack>, ArrayList<Object>>() {
            @Override
            public ArrayList<Object> call(ArrayList<SoundTrack> soundTracks) {
                return new ArrayList<Object>(soundTracks);
            }
        });
    }

    public OkHttpClient getClient() {
        //TODO leak
//        Cache cache = (contextWeakRef.get()
//                .getApplicationContext()).getCache();
        return new OkHttpClient.Builder()
//                .cache(cache)
                .addInterceptor(new CacheControlApplicationInterceptor(contextRef)) //app
                .addNetworkInterceptor(new CacheControlApplicationInterceptor(contextRef)) //network
                .build();
    }

    /**
     * interface
     */
    public interface YoutubeService {
        //part=snippet
        @GET("search?key=" + AuthCustom.API_KEY)
        Observable<ArrayList<SoundTrack>> searchList(@Query("q") String query, @Query("part") String part);
    }
}
