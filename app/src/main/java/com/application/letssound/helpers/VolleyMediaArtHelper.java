package com.application.letssound.helpers;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;

import java.lang.ref.WeakReference;


/**
 * Created by davide on 25/07/16.
 */
public class VolleyMediaArtHelper implements Response.Listener<Bitmap>, Response.ErrorListener {

    private static VolleyMediaArtHelper instance;
    private static WeakReference<OnVolleyMediaArtCallbackInterface> listenerRef;
    private final WeakReference<Context> ctx;
    private RequestQueue requestQueue;
    private static final int MAX_HEIGHT = 200;
    private static final int MAX_WIDTH = 200;
    private String requestedUrl;
    private ImageLoader imageLoader;

    /**
     *
     * @param context
     */
    private VolleyMediaArtHelper(WeakReference<Context> context) {
        ctx = context;
        init();
    }

    /**
     *
     */
    private void init() {
        RequestQueue requestQueueLocal = getRequestQueue();
        imageLoader = new ImageLoader(requestQueueLocal,
                new ImageLoader.ImageCache() {
                    private final LruCache<String, Bitmap>
                            cache = new LruCache<String, Bitmap>(20);

                    @Override
                    public Bitmap getBitmap(String url) {
                        return cache.get(url);
                    }

                    @Override
                    public void putBitmap(String url, Bitmap bitmap) {
                        cache.put(url, bitmap);
                    }
                });
    }

    /**
     *
     * @param context
     * @return
     */
    public static synchronized VolleyMediaArtHelper getInstance(WeakReference<Context> context,
                                                                WeakReference<OnVolleyMediaArtCallbackInterface> listener) {
        listenerRef = listener;
        if (instance == null) {
            instance = new VolleyMediaArtHelper(context);
        }
        return instance;
    }

    /**
     *
     * @param mediaArtUri
     */
    public void retrieveMediaArtAsync(Uri mediaArtUri) {
        int maxHeight = MAX_HEIGHT;
        int maxWidth = MAX_WIDTH;
        requestedUrl = mediaArtUri.toString();
        ImageRequest request = new ImageRequest(mediaArtUri.toString(), this, maxWidth, maxHeight,
            null, null, this);
        addToRequestQueue(request);
    }

    /**
     *
     * @return
     */
    public ImageLoader getImageLoader() {
        return imageLoader;
    }
    /**
     *
     * @return
     */
    private RequestQueue getRequestQueue() {
        return requestQueue == null ?
                requestQueue = Volley.newRequestQueue(ctx.get().getApplicationContext()) : requestQueue;
    }

    /**
     *
     * @param req
     * @param <T>
     */
    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    @Override
    public void onResponse(Bitmap response) {
        if (listenerRef != null) {
            listenerRef.get().onVolleyMediaArtSuccess(response, requestedUrl);
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        if (listenerRef != null) {
            listenerRef.get().onVolleyMediaArtError(error);
        }
    }

    /**
     *
     */
    public interface OnVolleyMediaArtCallbackInterface {
        void onVolleyMediaArtSuccess(Bitmap response, String requestedUrl);
        void onVolleyMediaArtError(VolleyError error);
    }



}
