package com.application.letssound.interceptors;

import android.content.Context;
import android.util.Log;

import com.application.letssound.utils.ConnectivityUtils;

import java.io.IOException;
import java.lang.ref.WeakReference;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okio.Buffer;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.BufferedSource;

/**
 * Created by davide on 23/07/16.
 */
public class CacheControlApplicationInterceptor extends OkHttpClient implements Interceptor {
    private final WeakReference<Context> contextRef;
    private final String TAG = "CacheControlApplicationInterceptor";

    public CacheControlApplicationInterceptor(WeakReference<Context> ctx) {
        contextRef = ctx;
    }

//    @Override
//    public Call newCall(Request request) {
//        Log.e(TAG, "pre request");
//
//        if (!ConnectivityUtils.isConnected(contextRef)) {
//            Log.e("No connectivity", request.headers().toString());
//            return null;
//        }
//
//        return super.newCall(request);
//    }

    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException {
        Response originalResponse = chain.proceed(chain.request());
        Log.e(TAG, "interceptor");

//        if (!ConnectivityUtils.isConnected(contextRef)) {
        if (false) {
            //fake response
            final String sample = "sample";
            return new Response.Builder()
                    .body(new ResponseBody() {
                        @Override
                        public MediaType contentType() {
                            return MediaType.parse("json");
                        }

                        @Override
                        public long contentLength() {
                            return sample.length();
                        }

                        @Override
                        public BufferedSource source() {
                            Buffer buffer = new Buffer();
                            buffer.write(sample.getBytes());
                            return buffer;
                        }
                    })
                    .build();
        }
        return originalResponse;


//        Response originalResponse = chain.proceed(chain.request());
//        String headerValue = ConnectivityUtils.isConnected(contextRef) ?
//                "no-cache" :
//                "public, max-stale=2419200";
//
//        return originalResponse.Builder()
//                .header("Cache-Control", headerValue)//"public, max-age=" + 5000)
//                .build();
    }
}
